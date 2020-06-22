package at.roteskreuz.covidapp.util;

import static com.google.common.base.Preconditions.checkArgument;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import static java.util.Objects.requireNonNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import java.util.stream.Collectors;

//https://github.com/airlift/airlift/blob/7e81834ea90f45211f1e6a2ebfdb6fd050561d53/security/src/main/java/io/airlift/security/pem/PemReader.java
/**
 * Utility class to load a PEM file
 *
 * @author Bernhard Roessler
 */
public final class PemReader {

	static final Pattern PRIVATE_KEY_PATTERN = Pattern.compile(
			"-+BEGIN\\s+(?:(.*)\\s+)?PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+"
			+ // Header
			"([a-z0-9+/=\\r\\n]+)"
			+ // Base64 text
			"-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", // Footer
			CASE_INSENSITIVE);

	private static final byte[] VERSION_0_ENCODED = new byte[]{2, 1, 0};
	private static final byte[] EC_KEY_OID = encodeOid("1.2.840.10045.2.1");
	private static final int SEQUENCE_TAG = 0x30;
	private static final int OCTET_STRING_TAG = 0x04;
	private static final int OBJECT_IDENTIFIER_TAG = 0x06;

	/**
	 * Loads a private key
	 *
	 * @param encodedKey
	 * @param keyType
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static PrivateKey loadPrivateKey(byte[] encodedKey, String keyType) throws IOException, GeneralSecurityException {
		String privateKey = new String(encodedKey);
		Matcher matcher = PRIVATE_KEY_PATTERN.matcher(privateKey);
		if (matcher.find()) {
			privateKey = matcher.group(2);
		}

		try {
			encodedKey = Base64.getDecoder().decode(privateKey.replace("\n", ""));
			encodedKey = ecPkcs1ToPkcs8(encodedKey);
		} catch (IllegalArgumentException ex) {
			// encoded Key is a Pkcs8 format
		}

		KeyFactory keyFactory = KeyFactory.getInstance(keyType);
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}

	static byte[] ecPkcs1ToPkcs8(byte[] pkcs1) throws GeneralSecurityException, IOException {
		List<byte[]> elements = decodeSequence(pkcs1);
		if (elements.size() != 4) {
			throw new InvalidKeySpecException("Expected EC key to have 4 elements");
		}
		byte[] curveOid = decodeSequenceOptionalElement(elements.get(2));
		byte[] keyIdentifier = encodeSequence(EC_KEY_OID, curveOid);
		return encodeSequence(VERSION_0_ENCODED, keyIdentifier, encodeOctetString(encodeSequence(elements.get(0), elements.get(1), elements.get(3))));
	}

	/**
	 * Encodes a sequence of bytes
	 * @param encodedValues
	 * @return
	 * @throws IOException
	 */
	public static byte[] encodeSequence(byte[]... encodedValues) throws IOException {
		int length = 0;
		for (byte[] encodedValue : encodedValues) {
			length += encodedValue.length;
		}
		byte[] lengthEncoded = encodeLength(length);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(SEQUENCE_TAG);
		out.write(lengthEncoded);
		for (byte[] entry : encodedValues) {
			out.write(entry);
		}
		return out.toByteArray();
	}

	/**
	 * Decodes a sequence of encoded values.
	 *
	 * @param sequence
	 * @return
	 */
	public static List<byte[]> decodeSequence(byte[] sequence) {
		int index = 0;

		// check tag
		checkArgument(sequence[0] == SEQUENCE_TAG, "Expected sequence tag");
		index++;

		// read length
		int sequenceDataLength = decodeLength(sequence, index);
		index += encodedLengthSize(sequenceDataLength);
		checkArgument(sequenceDataLength + index == sequence.length, "Invalid sequence");

		// read elements
		List<byte[]> elements = new ArrayList<>();
		while (index < sequence.length) {
			int elementStart = index;

			// skip the tag
			index++;

			// read length
			int length = decodeLength(sequence, index);
			index += encodedLengthSize(length);

			byte[] data = Arrays.copyOfRange(sequence, elementStart, index + length);
			elements.add(data);
			index += length;
		}
		return elements;
	}

	/**
	 * Decodes a optional element of a sequence.
	 *
	 * @param element
	 * @return
	 */
	public static byte[] decodeSequenceOptionalElement(byte[] element) {
		int index = 0;

		// check tag
		checkArgument((element[0] & 0xE0) == 0xA0, "Expected optional sequence element tag");
		index++;

		// read length
		int length = decodeLength(element, index);
		index += encodedLengthSize(length);
		checkArgument(length + index == element.length, "Invalid optional sequence element");

		return Arrays.copyOfRange(element, index, index + length);
	}

	/**
	 * Encodes an octet string.
	 *
	 * @param value
	 * @return
	 * @throws java.io.IOException
	 */
	public static byte[] encodeOctetString(byte[] value) throws IOException {
		byte[] lengthEncoded = encodeLength(value.length);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(OCTET_STRING_TAG);
		out.write(lengthEncoded);
		out.write(value);
		return out.toByteArray();
	}

	/**
	 * Encodes the length of a DER value.The encoding of a 7bit value is simply
	 * the value.Values needing more than 7bits are encoded as a lead byte with
	 * the high bit set and containing the number of value bytes. Then the
	 * following bytes encode the length using the least number of bytes
	 * possible.
	 *
	 * @param length
	 * @return
	 */
	public static byte[] encodeLength(int length) {
		if (length < 128) {
			return new byte[]{(byte) length};
		}
		int numberOfBits = 32 - Integer.numberOfLeadingZeros(length);
		int numberOfBytes = (numberOfBits + 7) / 8;
		byte[] encoded = new byte[1 + numberOfBytes];
		encoded[0] = (byte) (numberOfBytes | 0x80);
		for (int i = 0; i < numberOfBytes; i++) {
			int byteToEncode = (numberOfBytes - i);
			int shiftSize = (byteToEncode - 1) * 8;
			encoded[i + 1] = (byte) (length >>> shiftSize);
		}
		return encoded;
	}

	private static int encodedLengthSize(int length) {
		if (length < 128) {
			return 1;
		}
		int numberOfBits = 32 - Integer.numberOfLeadingZeros(length);
		int numberOfBytes = (numberOfBits + 7) / 8;
		return numberOfBytes + 1;
	}

	static int decodeLength(byte[] buffer, int offset) {
		int firstByte = buffer[offset] & 0xFF;
		checkArgument(firstByte != 0x80, "Indefinite lengths not supported in DER");
		checkArgument(firstByte != 0xFF, "Invalid length first byte 0xFF");
		if (firstByte < 128) {
			return firstByte;
		}

		int numberOfBytes = firstByte & 0x7F;
		checkArgument(numberOfBytes <= 4);

		int length = 0;
		for (int i = 0; i < numberOfBytes; i++) {
			length = (length << 8) | (buffer[offset + 1 + i] & 0xFF);
		}
		return length;
	}

	public static byte[] encodeOid(String oid) {
		requireNonNull(oid, "oid is null");

		List<Integer> parts = Arrays.stream(oid.split("\\."))
				.map(Integer::parseInt).collect(Collectors.toList());

		try {
			ByteArrayOutputStream body = new ByteArrayOutputStream();
			body.write(parts.get(0) * 40 + parts.get(1));
			for (Integer part : parts.subList(2, parts.size())) {
				writeOidPart(body, part);
			}

			byte[] length = encodeLength(body.size());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(OBJECT_IDENTIFIER_TAG);
			out.write(length);
			body.writeTo(out);
			return out.toByteArray();
		} catch (IOException e) {
			// this won't happen with byte array output streams
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Encode an OID number part. The encoding is a big endian varint.
	 */
	private static void writeOidPart(ByteArrayOutputStream out, final int number) {
		if (number < 128) {
			out.write((byte) number);
			return;
		}

		int numberOfBits = Integer.SIZE - Integer.numberOfLeadingZeros(number);
		int numberOfParts = (numberOfBits + 6) / 7;
		for (int i = 0; i < numberOfParts - 1; i++) {
			int partToEncode = (numberOfParts - i);
			int shiftSize = (partToEncode - 1) * 7;
			int part = (number >>> shiftSize) & 0x7F | 0x80;
			out.write(part);
		}
		out.write(number & 0x7f);
	}
}
