<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <div class="row m-0">
            <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                <jsp:include page="/components/sideBar.jsp" />
            </div>
            <div class="col-9 m-0 p-2" style="width: 80%">
                <div class="border border-gray shadow p-3 pb-5 bg-white rounded">
                    <div class="p-2">
                        <h3 class="mb-4 text-center">Reports</h3>
                        <div class="container mt-1">
                            <div style="display: flex; justify-content: space-between; align-items: center; gap: 12px;">
                                <div class="report-card shadow"
                                    style="width: 30%; border: 1px solid #ccc; padding: 20px; border-radius: 5px; margin: 0 auto; background-color: #f8f9fa;">
                                    <div class="pb-2"
                                        style="display: flex; justify-content: space-between;align-items: center;">
                                        <div>
                                            <h2 class="report-title text-md pb-1">Batch Reports</h2>
                                            <p class="card-text text-muted text-sm pb-1">Total batches: ${batchSize}</p>
                                        </div>
                                        <div class="me-2">
                                            <i class="fa-solid fa-chart-line fa-2x" style="color: #0F4C81;"></i>
                                        </div>
                                    </div>
                                    <a href="batchReport" class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                        role="button">View reports</a>
                                </div>

                                <div class="report-card shadow"
                                    style="width: 30%; border: 1px solid #ccc; padding: 20px; border-radius: 5px; margin: 0 auto; background-color: #f8f9fa;">
                                    <div class="pb-2"
                                        style="display: flex; justify-content: space-between;align-items: center;">
                                        <div>
                                            <h2 class="report-title text-md pb-1">Lab Reports</h2>
                                            <p class="card-text text-muted text-sm pb-1">Total labs: ${labSize}</p>
                                        </div>
                                        <div class="me-2">
                                            <i class="fa-solid fa-chart-area fa-2x" style="color: #0F4C81;"></i>
                                        </div>
                                    </div>
                                    <a href="viewlabreport" class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                        role="button">View
                                        reports</a>
                                </div>

                                <div class="report-card shadow"
                                    style="width: 30%; border: 1px solid #ccc; padding: 20px; border-radius: 5px; margin: 0 auto; background-color: #f8f9fa;">
                                    <div class="pb-2"
                                        style="display: flex; justify-content: space-between;align-items: center;">
                                        <div>
                                            <h2 class="report-title text-md pb-1">LabTest Reports</h2>
                                            <p class="card-text text-muted text-sm pb-1">Total labTests: ${labTestSize}
                                            </p>
                                        </div>
                                        <div class="me-2">
                                            <i class="fa-solid fa-chart-column fa-2x" style="color: #0F4C81;"></i>
                                        </div>
                                    </div>
                                    <a href="testReport" class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                        role="button">View reports</a>
                                </div>


                                <div class="report-card shadow"
                                    style="width: 30%; border: 1px solid #ccc; padding: 20px; border-radius: 5px; margin: 0 auto; background-color: #f8f9fa;">
                                    <div class="pb-2"
                                        style="display: flex; justify-content: space-between;align-items: center;  ">
                                        <div>
                                            <h2 class="report-title text-md pb-1">Trainee Reports</h2>
                                            <p class="card-text text-muted text-sm pb-1">Total Trainees: ${traineeSize}
                                            </p>
                                        </div>
                                        <div class="me-2">
                                            <i class="fa-solid fa-person fa-2x" style="color: #0F4C81;"></i>
                                        </div>
                                    </div>
                                    <a href="traineeReport" class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                        role="button">View reports</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>