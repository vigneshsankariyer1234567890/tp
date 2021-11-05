package teletubbies.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import teletubbies.commons.core.Range;
import teletubbies.commons.core.index.Index;
import teletubbies.commons.util.StringUtil;
import teletubbies.logic.parser.exceptions.ParseException;
import teletubbies.model.person.Address;
import teletubbies.model.person.Email;
import teletubbies.model.person.Name;
import teletubbies.model.person.Phone;
import teletubbies.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_RANGE_FORMAT = "Range must be in the format 'startIndex - endIndex'"
            + " or 'index1, index2,..., indexN'.";
    public static final String MESSAGE_INVALID_RANGE_FORMAT = "Range limits are in incorrect format!\n"
            + MESSAGE_RANGE_FORMAT;
    public static final String MESSAGE_INVALID_RANGE_INTEGERS = "Range values must be positive integers!\n";
    public static final String MESSAGE_INVALID_RANGE_LIMITS = "Left range limit must be smaller than right limit!\n";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a range String into a {@code Range} and returns it.
     *
     * @param range range values separated by a '-'
     * @return Range object
     * @throws ParseException If range is not 2 values separated by
     * a hyphen with the left value smaller than the right
     */
    public static Range parseRangeSeparatedByHyphen(String range) throws ParseException {
        requireNonNull(range);
        String[] rangeValues = range.split("-");
        if (rangeValues.length != 2) {
            throw new ParseException(MESSAGE_INVALID_RANGE_FORMAT);
        }
        String rangeValueLeft = rangeValues[0].trim();
        String rangeValueRight = rangeValues[1].trim();
        boolean areValuesIntegers = StringUtil.isNonZeroUnsignedInteger(rangeValueLeft)
                && StringUtil.isNonZeroUnsignedInteger(rangeValueRight);

        if (!areValuesIntegers) {
            throw new ParseException(MESSAGE_INVALID_RANGE_INTEGERS);
        }

        int start = Integer.parseInt(rangeValueLeft);
        int end = Integer.parseInt(rangeValueRight);

        if (start > end) {
            throw new ParseException(MESSAGE_INVALID_RANGE_LIMITS);
        }
        return new Range(IntStream.rangeClosed(start, end).boxed().collect(Collectors.toSet()));
    }

    /**
     * Parses a range (separated by commas) into a {@code Range} and returns it.
     *
     * @param range Index values separated by commas
     * @return Range object
     * @throws ParseException If range values are not integers
     */
    public static Range parseRangeSeparatedByCommas(String range) throws ParseException {
        requireNonNull(range);
        String[] rangeValues = range.split(",");
        Stream<String> trimmedValues = Arrays.stream(rangeValues).map(String::trim);
        if (!trimmedValues.allMatch(StringUtil::isNonZeroUnsignedInteger)) {
            throw new ParseException(MESSAGE_INVALID_RANGE_INTEGERS);
        }
        Set<Integer> rangeIntegers = Arrays.stream(rangeValues).map(String::trim)
                .map(Integer::parseInt).collect(Collectors.toSet());
        return new Range(rangeIntegers);
    }

    /**
     * Parses a valid range representation to a {@code Range} and returns it.
     *
     * @param range range in a valid format
     * @return Range object
     * @throws ParseException if invalid range
     */
    public static Range parseRange(String range) throws ParseException {
        return range.contains("-")
                ? ParserUtil.parseRangeSeparatedByHyphen(range)
                : ParserUtil.parseRangeSeparatedByCommas(range);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a colon separated {@code String tag} with name and value
     * into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * e.g. parseTagWithValue("Priority:HIGH");
     *
     * @throws ParseException if the given {@code tag} is invalid.
    */
    public static Tag parseTagWithValue(String tag) throws ParseException {
        requireNonNull(tag);
        String[] nameValuePair = tag.trim().split(":");
        String name = nameValuePair[0].trim();
        String value = nameValuePair.length < 2
                ? ""
                : nameValuePair[1].trim();
        if (!Tag.isAlphanumericTagName(name)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(name, value, Tag.getRolesForEditAccess(false), true);
    }

    /**
     * Parses {@code Collection<String> tags} with name and value into
     * a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTagsWithValue(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String v: tags) {
            tagSet.add(parseTagWithValue(v));
        }
        return tagSet;
    }

}
