package co.yiiu.module.log.repository;

import co.yiiu.module.log.model.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Integer> {

}
