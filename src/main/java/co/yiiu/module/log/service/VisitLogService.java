package co.yiiu.module.log.service;

import co.yiiu.core.util.DateUtil;
import co.yiiu.module.log.model.VisitLog;
import co.yiiu.module.log.model.VisitLogModel;
import co.yiiu.module.log.repository.VisitLogRepository;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class VisitLogService {
    @Autowired
    private VisitLogRepository visitLogRepository;

    @PersistenceContext
    private EntityManager em;

    public  void save(VisitLog visitLog){
        visitLogRepository.save(visitLog);
    }

    public List<VisitLogModel> findByGroupAddress(){
        List l = em.createQuery(
                "SELECT v.Address as name,count (v.id) as value FROM VisitLog v GROUP BY v.Address"
                        )
                .getResultList();
        List<VisitLogModel> list = new ArrayList<VisitLogModel>();
        for (Object p : l) {
            Object[] row = (Object[]) p;
            VisitLogModel model = new VisitLogModel();
            model.setName(row[0].toString());
            model.setValue(Integer.parseInt(row[1].toString()));
            list.add(model);
        }
        return list;

    }

    public List<VisitLogModel> findByGroupTime(){
        String d = DateUtil.getMonthBefore(new Date(),1);
        List l = em.createQuery(
                "SELECT DATE_FORMAT(v.InTime,'%Y-%m-%d') as name,count (v.id) as value FROM VisitLog v where v.InTime >=  '"
                        +d+
                        "' GROUP BY DATE_FORMAT(v.InTime,'%Y-%m-%d') ORDER BY v.InTime"
        )
                .getResultList();
        List<VisitLogModel> list = new ArrayList<VisitLogModel>();
        for (Object p : l) {
            Object[] row = (Object[]) p;
            VisitLogModel model = new VisitLogModel();
            model.setName(row[0].toString());
            model.setValue(Integer.parseInt(row[1].toString()));
            list.add(model);
        }
        return list;

    }
}
