package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@.chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_RELATIONSHIP = "@father";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_APPOINTMENT = "01-05-2025 14:30";
    private static final String INVALID_APPOINTMENT = "2025-05-01 14:30";
    private static final JsonAdaptedEmergencyPerson VALID_EMERGENCY_CONTACT =
            new JsonAdaptedEmergencyPerson("Jane Doe", "98765432", "Mother");
    private static final JsonAdaptedEmergencyPerson INVALID_EMERGENCY_CONTACT =
            new JsonAdaptedEmergencyPerson("R@chel", "+651234", "@father");
    private static final List<JsonAdaptedTag> VALID_ALLERGY_TAGS = BENSON.getAllergyTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final List<JsonAdaptedTag> VALID_CONDITION_TAGS = BENSON.getConditionTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final List<JsonAdaptedTag> VALID_INSURANCE_TAGS = BENSON.getInsuranceTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS, VALID_INSURANCE_TAGS,
                        VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS, VALID_INSURANCE_TAGS,
                VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                        VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS, VALID_INSURANCE_TAGS,
                VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                        VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                 VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                         VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                        VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                 VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        // Create a new list of tags from the valid tags
        List<JsonAdaptedTag> invalidAllergyTags = new ArrayList<>(VALID_ALLERGY_TAGS);
        // Add an invalid tag to the list of allergy tags
        invalidAllergyTags.add(new JsonAdaptedTag(INVALID_TAG));

        // Pass all required parameters to the JsonAdaptedPerson constructor, but with invalid allergy tags
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                invalidAllergyTags, VALID_CONDITION_TAGS,
                VALID_INSURANCE_TAGS, VALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);

        // Ensure that an IllegalValueException is thrown when converting to model type
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmergencyContact_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                         VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                        VALID_INSURANCE_TAGS, VALID_APPOINTMENT, INVALID_EMERGENCY_CONTACT);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidAppointment_throwsIllegalValueException() {
        JsonAdaptedPerson person =
            new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                    VALID_ADDRESS, VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS, VALID_INSURANCE_TAGS,
                INVALID_APPOINTMENT, VALID_EMERGENCY_CONTACT);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    public void toModelType_nullEmergencyContact_returnsPersonWithNilEmergencyContact() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                 VALID_ALLERGY_TAGS, VALID_CONDITION_TAGS,
                VALID_INSURANCE_TAGS, VALID_APPOINTMENT, null);
        assertEquals(person.toModelType().getEmergencyContact(), Person.NIL_EMERGENCY_CONTACT);
    }
}
