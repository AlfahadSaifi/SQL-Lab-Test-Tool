package lab.repository.labEngineRepo;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface LabEngineRepo {
    boolean match(SqlRowSet rowSet1, SqlRowSet rowSet2);

    void populateDisplayData(List<List<String>> outputList, SqlRowSet rowSet);

    SqlRowSet executeAndExtract(String query, String tableName);

    SqlRowSet extract(String sqlQuery1);
}
