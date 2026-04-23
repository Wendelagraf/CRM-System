package se.yrgo.services.diary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.yrgo.domain.Action;

public class DiaryManagementServiceMockImpl implements DiaryManagementService {

    private Set<Action> allActions = new HashSet<>();

    @Override
    public void recordAction(Action action) {
        allActions.add(action);
    }

    @Override
    public List<Action> getAllIncompleteActions(String requiredUser) {

        List<Action> result = new ArrayList<>();

        for (Action action : allActions) {
            if (action.getOwningUser().equals(requiredUser) && !action.isComplete()) {
                result.add(action);
            }
        }

        return result;
    }
}