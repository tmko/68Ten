package com.ten68.marketing.webfront.socket;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StructMessageTest {

    @Test
    void constructor_setsTimestampAndMessage() {
        StructMessage msg = new StructMessage(12345L, "test message");
        assertThat(msg.timestamp()).isEqualTo(12345L);
        assertThat(msg.message()).isEqualTo("test message");
    }

    @Test
    void compareTo_equalTimestamps_returnsZero() {
        StructMessage a = new StructMessage(100L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.compareTo(b)).isZero();
    }

    @Test
    void compareTo_greaterTimestamp_returnsPositive() {
        StructMessage a = new StructMessage(200L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.compareTo(b)).isPositive();
    }

    @Test
    void compareTo_lesserTimestamp_returnsNegative() {
        StructMessage a = new StructMessage(50L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.compareTo(b)).isNegative();
    }

    @Test
    void isLater_returnsTrueWhenTimestampGreater() {
        StructMessage a = new StructMessage(200L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.isLater(b)).isTrue();
    }

    @Test
    void isLater_returnsFalseWhenTimestampEqual() {
        StructMessage a = new StructMessage(100L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.isLater(b)).isFalse();
    }

    @Test
    void isLater_returnsFalseWhenTimestampLess() {
        StructMessage a = new StructMessage(50L, "a");
        StructMessage b = new StructMessage(100L, "b");
        assertThat(a.isLater(b)).isFalse();
    }

    @Test
    void naturalOrder_sortsByTimestampAscending() {
        List<StructMessage> messages = List.of(
                new StructMessage(300L, "c"),
                new StructMessage(100L, "a"),
                new StructMessage(200L, "b")
        );
        List<StructMessage> sorted = messages.stream().sorted().toList();
        assertThat(sorted.get(0).timestamp()).isEqualTo(100L);
        assertThat(sorted.get(1).timestamp()).isEqualTo(200L);
        assertThat(sorted.get(2).timestamp()).isEqualTo(300L);
    }

    @Test
    void reverseOrder_sortsByTimestampDescending() {
        List<StructMessage> messages = List.of(
                new StructMessage(100L, "a"),
                new StructMessage(300L, "c"),
                new StructMessage(200L, "b")
        );
        List<StructMessage> sorted = messages.stream().sorted(Comparator.reverseOrder()).toList();
        assertThat(sorted.get(0).timestamp()).isEqualTo(300L);
        assertThat(sorted.get(1).timestamp()).isEqualTo(200L);
        assertThat(sorted.get(2).timestamp()).isEqualTo(100L);
    }

    @Test
    void build_validInput_returnsStructMessage() {
        StructMessage result = StructMessage.build("1782805743589 hello world");
        assertThat(result).isNotSameAs(StructMessage.INVALID);
        assertThat(result.timestamp()).isEqualTo(1782805743589L);
        assertThat(result.message()).isEqualTo("hello world");
    }

    @Test
    void build_nullInput_returnsInvalid() {
        assertThat(StructMessage.build(null)).isSameAs(StructMessage.INVALID);
    }

    @Test
    void build_blankInput_returnsInvalid() {
        assertThat(StructMessage.build("")).isSameAs(StructMessage.INVALID);
        assertThat(StructMessage.build("   ")).isSameAs(StructMessage.INVALID);
    }

    @Test
    void build_nonNumericFirstToken_returnsInvalid() {
        assertThat(StructMessage.build("abc hello world")).isSameAs(StructMessage.INVALID);
    }


    @Test
    void build_negativeTimestamp_returnsInvalid() {
        assertThat(StructMessage.build("-1 hello")).isSameAs(StructMessage.INVALID);
    }

    @Test
    void build_zeroTimestamp_withContent_returnsStructMessage() {
        StructMessage result = StructMessage.build("0 hello");
        assertThat(result).isNotSameAs(StructMessage.INVALID);
        assertThat(result.timestamp()).isZero();
        assertThat(result.message()).isEqualTo("hello");
    }

    @Test
    void INVALID_hasNegativeTimestamp() {
        assertThat(StructMessage.INVALID.timestamp()).isNegative();
        assertThat(StructMessage.INVALID.message()).isEqualTo("invalid");
    }

    @Test
    void equals_comparesTimestampAndMessage() {
        StructMessage a = new StructMessage(100L, "test");
        StructMessage b = new StructMessage(100L, "test");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
