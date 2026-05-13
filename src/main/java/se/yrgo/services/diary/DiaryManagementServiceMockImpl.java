package se.yrgo.services.diary;

import org.springframework.stereotype.Service;
import se.yrgo.domain.Action;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryManagementServiceMockImpl implements DiaryManagementService {

    private List<Action> actions = new ArrayList<>();

    @Override
    public void recordAction(Action action) {
        actions.add(action);
    }

    @Override
    public List<Action> getAllIncompleteActions(String requiredUser) {
        return actions.stream()
                .filter(a -> !a.isComplete())
                .filter(a -> a.getOwningUser().equals(requiredUser))
                .toList();
    }
}