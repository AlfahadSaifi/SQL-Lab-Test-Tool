package lab.repository.admin;

import lab.convert.Convert;
import lab.dto.admin.AdminDto;
import lab.dto.admin.BatchDto;
import lab.dto.lab.LabDto;
import lab.dto.lab.LabTestDto;
import lab.dto.questionBank.QuestionDto;
import lab.dto.trainee.TraineeDetailDto;
import lab.dto.trainee.TraineeDto;
import lab.dto.user.UserDto;
import lab.entity.admin.Admin;
import lab.entity.lab.Lab;
import lab.entity.lab.LabTest;
import lab.entity.questionBank.Question;
import lab.entity.trainee.Trainee;
import lab.entity.trainee.TraineeDetail;
import lab.entity.user.User;
import lab.exceptionHandler.CustomDatabaseException;
import lab.exceptions.user.UserAlreadyExist;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static lab.gobal.HibernateUtil.getTableName;

@Repository
public class AdminImpl implements AdminRepo {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean saveTrainee(List<TraineeDto> studentDtos) {
        boolean flag = false;
        List<Trainee> studentList = Convert.getModelMapper().toEntityList(studentDtos, Trainee.class);
        if (!studentList.isEmpty()) {
            Session session = sessionFactory.getCurrentSession();
            int batchSize = 5;
            for (int i = 0; i < studentList.size(); i++) {
                session.save(studentList.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
            flag = true;
        }
        return flag;
    }

    @Override
    public TraineeDto saveTrainee(TraineeDto traineeDto) {
        Session session = sessionFactory.getCurrentSession();
        Trainee trainee = Convert.getModelMapper().toEntity(traineeDto, Trainee.class);
        session.saveOrUpdate(trainee);
        traineeDto = Convert.getModelMapper().toDto(traineeDto, TraineeDto.class);
        return traineeDto;
    }

    @Override
    public boolean addQuestion(QuestionDto questionDto) {
        Question question = Convert.getModelMapper().toEntity(questionDto, Question.class);
        Session session = sessionFactory.getCurrentSession();
        int id = (int) session.save(question);
        return id != 0;
    }

    @Override
    public boolean addQuestion(List<QuestionDto> questionDtos) {
        boolean flag = false;
        List<Question> questionList = Convert.getModelMapper().toEntityList(questionDtos, Question.class);
        if (!questionList.isEmpty()) {
            Session session = sessionFactory.getCurrentSession();
            int batchSize = 5;
            for (int i = 0; i < questionList.size(); i++) {
                session.save(questionList.get(i));
                if (i % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }
            flag = true;
        }
        return flag;
    }

    @Override
    public void saveAdmin(AdminDto admin) throws UserAlreadyExist {
        Admin admin1 = Convert.getModelMapper().toEntity(admin, Admin.class);
        Session session = sessionFactory.getCurrentSession();
        try {
            session.save(admin1);
            session.flush();
        } catch (HibernateException exception) {
            exception.printStackTrace();
            throw new UserAlreadyExist("User Already Registered.");
        }
        session.clear();
    }

    @Override
    public AdminDto getAdminByUserName(String username) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Admin A WHERE A.employeeId = :username";
        Query<Admin> query = session.createQuery(hql, Admin.class);
        query.setParameter("username", username);
        Admin admin = query.uniqueResult();
        AdminDto adminDto = Convert.getModelMapper().toDto(admin, AdminDto.class);
        session.evict(admin);
        return adminDto;
    }

    @Override
    public AdminDto updateAdmin(AdminDto adminDto) throws CustomDatabaseException {
        Session session = sessionFactory.getCurrentSession();
        Admin admin = Convert.getModelMapper().toEntity(adminDto, Admin.class);
        try {
            session.update(admin);
            adminDto = Convert.getModelMapper().toEntity(admin, AdminDto.class);
            return adminDto;
        } catch (HibernateException exception) {
            throw new CustomDatabaseException("admin already exist", exception);
        }
    }

    @Override
    public List<BatchDto> getBatchDtoList(String username) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "Select b.id, b.batchCode from Admin a join a.batches b where a.employeeId = :username";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("username", username);
        List<Object[]> batchDataList = query.getResultList();
        List<BatchDto> batchDtoList = new ArrayList<>();
        for (Object[] batchData : batchDataList) {
            Integer batchId = (Integer) batchData[0];
            String batchCode = (String) batchData[1];
            BatchDto batchDto = new BatchDto();
            batchDto.setId(batchId);
            batchDto.setBatchCode(batchCode);
            batchDtoList.add(batchDto);
        }
        return batchDtoList;
    }

    @Override
    public List<LabDto> getLabDtoList(String labName) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "Select l.labId, l.labName from Admin a join a.labs l where a.employeeId = :labName";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("labName", labName);
        List<Object[]> labDataList = query.getResultList();
        List<LabDto> labDtoList = new ArrayList<>();
        for (Object[] labData : labDataList) {
            int labId = (int) labData[0];
            String labCode = (String) labData[1];
            LabDto labDto = new LabDto();
            labDto.setLabId(labId);
            labDto.setLabName(labCode);
            labDtoList.add(labDto);
        }
        return labDtoList;
    }

    @Override
    public LabDto addLabToAdmin(LabDto labDto, String adminId) {
        Lab lab = Convert.getModelMapper().toEntity(labDto, Lab.class);
        Session session = sessionFactory.getCurrentSession();
        session.save(lab);
        Admin admin = (Admin)session.createQuery("from Admin where employeeId = :adminId")
                .setParameter("adminId", adminId).uniqueResult();
        admin.getLabs().add(lab);
        return Convert.getModelMapper().toDto(lab, LabDto.class);
    }

    @Override
    public LabTestDto addLabTestInAdmin(LabTestDto labTestDto, String adminId) {
        LabTest labTest = Convert.getModelMapper().toEntity(labTestDto, LabTest.class);
        Session session = sessionFactory.getCurrentSession();
        session.save(labTest);
        Admin admin = (Admin)session.createQuery("from Admin where employeeId = :adminId")
                .setParameter("adminId", adminId).uniqueResult();
        admin.getLabTests().add(labTest);
        return Convert.getModelMapper().toDto(labTest, LabTestDto.class);
    }

    @Override
    public long getAdminIdByUserName(String userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT u.id FROM User u WHERE u.employeeId = :empId";
        Query<Long> query = session.createQuery(hql, Long.class);

        query.setParameter("empId", userId);

        Long adminId = query.uniqueResult();

        return adminId != null ? adminId.longValue() : 0;
    }

    @Override
    public UserDto createTraineeInUser(UserDto userDto) {
        User user = Convert.getModelMapper().toEntity(userDto, User.class);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(user);
            userDto = Convert.getModelMapper().toDto(user, UserDto.class);
            return userDto;
        } catch (HibernateException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public void insertIntoTrainee(Long id, int batchId) {
        Session session = sessionFactory.getCurrentSession();
        String tableName = getTableName(Trainee.class, sessionFactory);
        String nativeSQLInsert = "INSERT INTO " + tableName + " (BATCHID, ID) " +
                "VALUES (:batchId, :id)";
        SQLQuery query = session.createSQLQuery(nativeSQLInsert);
        query.setParameter("batchId", batchId);
        query.setParameter("id", id);
        int rowsAffected = query.executeUpdate();
    }

    @Override
    public TraineeDetailDto getTranieeDetailByEmpId(String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM TraineeDetail WHERE employeeId = :empId";
        TraineeDetail traineeDetail = session.createQuery(hql, TraineeDetail.class)
                .setParameter("empId", employeeId)
                .uniqueResult();
        if(traineeDetail==null) return null;
        TraineeDetailDto traineeDetailDto = Convert.getModelMapper().toDto(traineeDetail, TraineeDetailDto.class);
        return traineeDetailDto;

    }

    @Override
    public TraineeDetailDto editTranieeDetailByEmpId(TraineeDetailDto updatedTraineeDetail, String employeeId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            String hql = "UPDATE TraineeDetail SET designation = :designation, doj = :doj, grade = :grade, ibu = :ibu, " +
                    "function = :function, tierCategorization = :tierCategorization, probationPeriod = :probationPeriod, " +
                    "personalEmailId = :personalEmailId, contactNumber = :contactNumber, dob = :dob, io = :io, " +
                    "tenthPercent = :tenthPercent, twelfthPercent = :twelfthPercent, graduation = :graduation, " +
                    "graduationPercent = :graduationPercent, branch = :branch, graduationYOP = :graduationYOP, " +
                    "collegeName = :collegeName, university = :university, universityShortName = :universityShortName, " +
                    "address = :address, city = :city, state = :state, country = :country, pinCode = :pinCode, " +
                    "userId = :userId, confirmationDate = :confirmationDate WHERE employeeId = :empId";

            int rowsUpdated = session.createQuery(hql)
                    .setParameter("designation", updatedTraineeDetail.getDesignation())
                    .setParameter("doj", updatedTraineeDetail.getDoj())
                    .setParameter("grade", updatedTraineeDetail.getGrade())
                    .setParameter("ibu", updatedTraineeDetail.getIbu())
                    .setParameter("function", updatedTraineeDetail.getFunction())
                    .setParameter("tierCategorization", updatedTraineeDetail.getTierCategorization())
                    .setParameter("probationPeriod", updatedTraineeDetail.getProbationPeriod())
                    .setParameter("personalEmailId", updatedTraineeDetail.getPersonalEmailId())
                    .setParameter("contactNumber", updatedTraineeDetail.getContactNumber())
                    .setParameter("dob", updatedTraineeDetail.getDob())
                    .setParameter("io", updatedTraineeDetail.getIo())
                    .setParameter("tenthPercent", updatedTraineeDetail.getTenthPercent())
                    .setParameter("twelfthPercent", updatedTraineeDetail.getTwelfthPercent())
                    .setParameter("graduation", updatedTraineeDetail.getGraduation())
                    .setParameter("graduationPercent", updatedTraineeDetail.getGraduationPercent())
                    .setParameter("branch", updatedTraineeDetail.getBranch())
                    .setParameter("graduationYOP", updatedTraineeDetail.getGraduationYOP())
                    .setParameter("collegeName", updatedTraineeDetail.getCollegeName())
                    .setParameter("university", updatedTraineeDetail.getUniversity())
                    .setParameter("universityShortName", updatedTraineeDetail.getUniversityShortName())
                    .setParameter("address", updatedTraineeDetail.getAddress())
                    .setParameter("city", updatedTraineeDetail.getCity())
                    .setParameter("state", updatedTraineeDetail.getState())
                    .setParameter("country", updatedTraineeDetail.getCountry())
                    .setParameter("pinCode", updatedTraineeDetail.getPinCode())
                    .setParameter("userId", updatedTraineeDetail.getUserId())
                    .setParameter("confirmationDate", updatedTraineeDetail.getConfirmationDate())
                    .setParameter("empId", employeeId)
                    .executeUpdate();
            int res = rowsUpdated;
            return updatedTraineeDetail;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public TraineeDetailDto editTranieeDetailById(TraineeDetailDto traineeDetailDto) {
        Session session =sessionFactory.getCurrentSession();
        TraineeDetail traineeDetail = Convert.getModelMapper().toEntity(traineeDetailDto, TraineeDetail.class);
        session.saveOrUpdate(traineeDetail);
        traineeDetailDto =Convert.getModelMapper().toDto(traineeDetail,TraineeDetailDto.class);
        return traineeDetailDto;
    }

    @Override
    public void changePassword(String newPassword, String employeeId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "UPDATE User SET password = :newPassword WHERE employeeId = :empId";
        int rowsUpdated = session.createQuery(hql)
                .setParameter("newPassword", newPassword)
                .setParameter("empId", employeeId)
                .executeUpdate();
    }

    @Override
    public boolean removeLab(int labId, String userName) {
        Session session = sessionFactory.getCurrentSession();
        Admin admin = session.get(Admin.class, getAdminIdByUserName(userName));
        Lab lab = session.get(Lab.class, labId);
        admin.getLabs().remove(lab);
        session.delete(lab);
        return true;
    }

    @Override
    public boolean removeLabTest(int labTestId, String userName) {
        Session session = sessionFactory.getCurrentSession();
        Admin admin = session.get(Admin.class, getAdminIdByUserName(userName));
        LabTest labTest = session.get(LabTest.class, labTestId);
        admin.getLabTests().remove(labTest);
        session.delete(labTest);
        return true;
    }
}
