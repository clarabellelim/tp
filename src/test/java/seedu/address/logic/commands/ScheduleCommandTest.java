package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ArchivedBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ScheduleCommand.
 */
public class ScheduleCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs(), new ArchivedBook());

    @Test
    public void execute_validIndex_success() throws Exception {
        Person personToSchedule = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Appointment newAppointment = new Appointment("18-03-2025 10:00");
        ScheduleCommand scheduleCommand = new ScheduleCommand(INDEX_FIRST_PERSON, newAppointment);
        String expectedMessage = ScheduleCommand.MESSAGE_SUCCESS;

        // Create an updated person with the new appointment.
        Person scheduledPerson = personToSchedule.withAppointment(newAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
            new ArchivedBook(model.getArchivedBook()));
        expectedModel.setPerson(personToSchedule, scheduledPerson);
        assertCommandSuccess(scheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Appointment newAppointment = new Appointment("18-03-2025 10:00");
        ScheduleCommand scheduleCommand = new ScheduleCommand(outOfBoundIndex, newAppointment);

        assertCommandFailure(scheduleCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
