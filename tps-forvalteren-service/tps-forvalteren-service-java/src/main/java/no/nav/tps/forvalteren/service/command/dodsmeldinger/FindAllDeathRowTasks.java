package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAllDeathRowTasks {

    @Autowired
    private DeathRowRepository repository;

    public List<List<DeathRow>> execute() {

        List<List<DeathRow>> completeDeathRowTaskList = new ArrayList<List<DeathRow>>();
        List<DeathRow> allDeathRowTaskD = removeCompletedTasks(repository.findAllByHandling("D"));
        List<DeathRow> allDeathRowTaskC = removeCompletedTasks(repository.findAllByHandling("C"));
        List<DeathRow> allDeathRowTaskU = repository.findAllByHandling("U");

        for(DeathRow updateDeathRowTask : allDeathRowTaskU) {
            allDeathRowTaskD.add(updateDeathRowTask);
            allDeathRowTaskC.add(updateDeathRowTask);
        }

        completeDeathRowTaskList.add(allDeathRowTaskD);
        completeDeathRowTaskList.add(allDeathRowTaskC);

        return completeDeathRowTaskList;
    }

    private List<DeathRow> removeCompletedTasks(List<DeathRow> listToBeChecked){
        List<DeathRow> uncompletedTasks = new ArrayList<>();
        for(DeathRow task : listToBeChecked){
            if(task.getHandling() != task.getStatus()){
                uncompletedTasks.add(task);
            }
        }
        return uncompletedTasks;
    }

}
