package org.example.dao;

import org.example.dto.ChartData;
import org.example.dto.TimeChartData;
import org.example.entities.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    @Override
    List<Project> findAll();

    @Query(nativeQuery=true, value="SELECT stage as label, COUNT(*) as value " +
            "FROM project " +
            "GROUP BY stage")
    List<ChartData> getProjectStatus();

    @Query(nativeQuery=true, value="SELECT name as projectName, start_date as startDate, end_date as endDate, status as status, progress as progress"
            + " FROM project WHERE start_date is not null")
    List<TimeChartData> getTimeData();

    @Query("SELECT p.name FROM Project p JOIN p.employees e WHERE e.id = :employeeId")
    List<String> findProjectNamesByEmployeeId(Long employeeId);

    @Query("SELECT p FROM Project p JOIN p.employees e WHERE e.employeeId = :employeeId")
    List<Project> findProjectsByEmployeeId(Long employeeId);
}