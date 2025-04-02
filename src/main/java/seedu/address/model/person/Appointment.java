package seedu.address.model.person;

import java.util.Objects;

/**
 * Represents a Schedule in the address book.
 * Guarantees: details are present and not null.
 */
public class Appointment implements Comparable<Appointment> {
    public static final String MESSAGE_CONSTRAINTS = "This must be DD-MM-YYYY HH:MM";
    private static final String DEFAULT_DATE_STRING = "01-01-9999 00:00";
    private static final DateTime DEFAULT_DATE = new DateTime(DEFAULT_DATE_STRING);

    public final String value;
    private final DateTime dateTime;
    private final String description;

    /**
     * Constructs an {@code Appointment} with a specified date and description.
     *
     * @param dateTime The date and time of the appointment.
     * @param description A brief description of the event.
     */
    public Appointment(DateTime dateTime, String description) {
        this.dateTime = Objects.requireNonNullElse(dateTime, DEFAULT_DATE);
        this.description = Objects.requireNonNullElse(description, "No description");
        this.value = this.dateTime.toString() + " " + this.description;
    }

    /**
     * Constructs an {@code Appointment} with a default date.
     */
    public Appointment() {
        this.dateTime = DEFAULT_DATE;
        this.description = "No appointment";
        this.value = DEFAULT_DATE.toString() + " " + description;
    }

    /**
     * Constructs an {@code Appointment} from a string.
     *
     * @param appointment A valid appointment string.
     */
    public Appointment(String appointment) {
        if (appointment == null || appointment.isBlank()) {
            this.dateTime = DEFAULT_DATE;
            this.value = DEFAULT_DATE_STRING;
            this.description = "No appointment";
        } else {
            this.dateTime = new DateTime(appointment);
            this.value = this.dateTime.toString();
            this.description = "";
        }
    }

    public static boolean isValid(String dateTime) {
        return DateTime.isValidDateTime(dateTime);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Appointment other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Appointment)) {
            return false;
        }
        Appointment otherAppointment = (Appointment) other;
        return dateTime.difference(otherAppointment.dateTime).abs().toMinutes() < 15.0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description);
    }

    @Override
    public String toString() {
        return dateTime.toString() + (description.isBlank() ? "" : " " + description);
    }
}
