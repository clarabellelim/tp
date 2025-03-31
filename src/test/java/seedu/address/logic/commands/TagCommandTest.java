package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.ArchivedBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class TagCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new ArchivedBook());

    @Test
    public void execute_addValidTag_success() {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> tagsToAdd = new HashSet<>(Collections.singleton(new Tag("peanuts")));
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd, new HashSet<>(),
                new HashSet<>(), new HashSet<>());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getArchivedBook());
        expectedModel.addTagsToPerson(personToTag, tagsToAdd);
        Person updatedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        CommandResult result = assertDoesNotThrow(() -> tagCommand.execute(model));

        // Compare only the updated person (avoiding ModelManager hashcodes)
        assertTrue(result.getFeedbackToUser().contains(updatedPerson.toString()),
                "Feedback should contain the updated person details with the new tag added.");
    }


    @Test
    public void execute_addExistingTag_throwsCommandException() {
        Person personToTag = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Tag> existingTags = personToTag.getTags();
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, existingTags, new HashSet<>(),
                                new HashSet<>(), new HashSet<>());

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_DUPLICATE_TAGS);
    }

    @Test
    public void execute_deleteValidTag_success() {
        Person personToTag = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Set<Tag> tagsToDelete = new HashSet<>(Collections.singleton(new Tag("asthma")));
        TagCommand tagCommand = new TagCommand(INDEX_SECOND_PERSON, new HashSet<>(), new HashSet<>(),
                new HashSet<>(), tagsToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), model.getArchivedBook());
        expectedModel.deleteTagFromPerson(personToTag, tagsToDelete);
        Person updatedPerson = expectedModel.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        CommandResult result = assertDoesNotThrow(() -> tagCommand.execute(model));

        assertFalse(updatedPerson.getTags().contains(new Tag("asthma")),
                "The asthma tag should be removed.");

        // Compare only the updated person's details in the feedback (ignoring ModelManager hashcodes).
        assertTrue(result.getFeedbackToUser().contains(updatedPerson.toString()),
                "Feedback should contain the updated person details with the tag removed.");
    }

    @Test
    public void execute_deleteNonExistentTag_throwsCommandException() {
        Set<Tag> tagsToDelete = new HashSet<>(Collections.singleton(new Tag("NonExistentTag")));
        TagCommand tagCommand = new TagCommand(INDEX_FIRST_PERSON, new HashSet<>(), new HashSet<>(),
                                new HashSet<>(), tagsToDelete);

        assertCommandFailure(tagCommand, model, TagCommand.MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, new HashSet<>(), new HashSet<>(),
                                new HashSet<>(), new HashSet<>());

        assertCommandFailure(tagCommand, model, "The patient index provided is invalid");
    }

    @Test
    public void equals() {
        Set<Tag> allergies = new HashSet<>(Collections.singleton(new Tag("Peanuts")));
        TagCommand tagFirstCommand = new TagCommand(INDEX_FIRST_PERSON, allergies, new HashSet<>(),
                                    new HashSet<>(), new HashSet<>());
        TagCommand tagSecondCommand = new TagCommand(INDEX_SECOND_PERSON, allergies, new HashSet<>(),
                                    new HashSet<>(), new HashSet<>());

        // same object -> returns true
        assertEquals(tagFirstCommand, tagFirstCommand);

        // same values -> returns true
        TagCommand tagFirstCommandCopy = new TagCommand(INDEX_FIRST_PERSON, allergies, new HashSet<>(),
                                        new HashSet<>(), new HashSet<>());
        assertEquals(tagFirstCommand, tagFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(new Object(), tagFirstCommand);

        // different person -> returns false
        assertNotEquals(tagFirstCommand, tagSecondCommand);
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        Set<Tag> allergies = new HashSet<>(Collections.singleton(new Tag("Peanuts")));
        TagCommand tagCommand = new TagCommand(targetIndex, allergies, new HashSet<>(),
                                new HashSet<>(), new HashSet<>());
        String expected = TagCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", allergies=" + allergies + ", conditions=[], insurances=[]}";
        assertEquals(expected, tagCommand.toString());
    }
}
