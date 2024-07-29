<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
      <%@ page isELIgnored="false" %>
        <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"
          integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz"
          crossorigin="anonymous"></script>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <div class="mx-5 my-2">
          <div class="">
            <h3 class="text-center ">View Practice Report</h3>
          </div>
          <div class="container-fluid shadow-sm p-3 bg-white rounded border-gray ">
            <div class="flexCenter">
              <div class="spinner-border text-primary" role="status" id="loadingSpinner" style="display: none;">
                <span class="sr-only">Loading...</span>
              </div>
              <div style="height: 223px; width: 446px;">
                <canvas id="statusChart" style="height: 223px; width: 446px;"></canvas>
              </div>
              <div class="d-flex g-lg-5 mx-5 my-3">
                <div class="px-5">
                  <i class="bi bi-check-circle text-success"></i>
                  <span id="totalPoints" class="ml-2"><b>Total Points: </b> </span>
                </div>
                <div class="px-5">
                  <i class="bi bi-check-circle text-success"></i>
                  <span id="correctQuestion" class="ml-2"><b>Correct:</b> </span>
                </div>
                <div class="px-5">
                  <i class="bi bi-x-circle text-danger"></i>
                  <span id="inCorrectQuestion" class="ml-2"><b>Incorrect:</b> </span>
                </div>
                <div class="px-5">
                  <i class="bi bi-question-circle text-secondary"></i>
                  <span id="unAttemptedQuestion" class="ml-2"><b>Unattempted:</b> </span>
                </div>
                <div class="px-5">
                  <button id="viewReportButton" class="py-2 px-3 rounded border bg-blue text-white text-sm"
                    type="button">View Detail Report</button>
                </div>
                <!-- <div class="px-5">
                        <i class="bi bi-arrow-right-circle text-warning"></i>
                        <span id="skipQuestion" class="ml-2"><b>Skip:</b> </span>
                    </div> -->
              </div>
            </div>

            <div class="container">
              <table id="reportTable" class="table">
                <thead>
                  <tr>
                    <th>Question No.</th>
                    <th>Status</th>
                    <th>Incorrect Attempt</th>
                    <!-- <th>Submit Query</th> -->
                  </tr>
                </thead>
                <tbody id="tableData"></tbody>
              </table>
            </div>
          </div>
        </div>
        <div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="reportModalLabel"
          aria-hidden="true">
          <div class="modal-dialog  modal-lg" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="reportModalLabel">Report Details</h5>
                <div class="cursor-pointer" onclick="closeReportModal()" aria-label="Close">
                  <i class="fa-regular fa-circle-xmark"></i>
                </div>
              </div>
              <div class="modal-body">
                <div class="reportModalBodyscroll" id="reportModalBody">
                  <div class="spinner-border text-primary" role="status">
                    <span class="sr-only">Loading...</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <script>
            function closeReportModal() {
              $('#reportModal').modal('hide');
            }
            $(document).ready(function () {
              const queryString = window.location.search;
              const urlParams = new URLSearchParams(queryString);
              const labId = urlParams.get('labId');
              $('#loadingSpinner').show();
              $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/api/trainee/getLabReport?labId=" + labId,
                contentType: "application/json",
                success: function (data) {
                  $("#scoreReport").append(data.obtainedPoint + "/" + data.totalPoints)
                  $('#loadingSpinner').hide();
                  let array = mergeQuestion(data.labInfo.questionStatusList, data.recordlabAttemptList)
                  renderTable(array);
                  let questionCount = countQuestion(data.labInfo.questionStatusList);
                  renderStatusCount(questionCount);
                  $("#reportTable").DataTable()
                },
                error: function (xhr, status, error) {
                  $('#loadingSpinner').hide();
                  console.error("Error:", error);
                },
              });
              function mergeQuestion(array1, array2) {
                const mergedArray = [];
                array1.forEach(obj1 => {
                  const matchingObj = array2.find(obj2 => obj1.questionId === obj2.questionId);
                  if (matchingObj) {
                    mergedArray.push({ ...obj1, ...matchingObj });
                  } else {
                    mergedArray.push({ ...obj1 });
                  }
                });
                return mergedArray;
              }
              function countQuestion(data) {
                const statusCount = {
                  INCORRECT: 0,
                  UNATTEMPTED: 0,
                  CORRECT: 0,
                };

                data.forEach(item => {
                  if (item.status === 'INCORRECT') {
                    statusCount.INCORRECT++;
                  } else if (item.status === 'UNATTEMPTED') {
                    statusCount.UNATTEMPTED++;
                  } else if (item.status === 'CORRECT') {
                    statusCount.CORRECT++;
                  }
                });
                return statusCount;
              }
              function renderTable(data) {
                const tableData = document.getElementById('tableData');
                data.forEach((item, index) => {
                  const row = document.createElement('tr');
                  const serialNumberCell = document.createElement('td');
                  serialNumberCell.textContent = index + 1;
                  row.appendChild(serialNumberCell);
                  ['status', 'incorrectAttempt'].forEach(key => {
                    const cell = document.createElement('td');
                    cell.textContent = item[key] || '0';
                    row.appendChild(cell);
                  });
                  tableData.appendChild(row);
                });
              }
              function renderStatusCount(statusCounts) {
                $("#correctQuestion").append(statusCounts.CORRECT)
                $("#inCorrectQuestion").append(statusCounts.INCORRECT)
                $("#unAttemptedQuestion").append(statusCounts.UNATTEMPTED)
                renderChart(statusCounts);
              }

              function renderChart(statusCounts) {
                const ctx = document.getElementById('statusChart').getContext('2d');
                new Chart(ctx, {
                  type: 'bar',
                  data: {
                    labels: ['Correct', 'Incorrect', 'Unattempted'],
                    datasets: [{
                      label: 'Question Status',
                      data: [statusCounts.CORRECT, statusCounts.INCORRECT, statusCounts.UNATTEMPTED, statusCounts.SKIP],
                      backgroundColor: [
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                      ],
                      borderColor: [
                        'rgba(75, 192, 192, 1)',
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                      ],
                      borderWidth: 1
                    }]
                  },
                  options: {
                    scales: {
                      y: {
                        beginAtZero: true
                      }
                    }
                  }
                });
                statusChart.canvas.parentNode.style.width = '200px';
                statusChart.canvas.parentNode.style.height = '50px';
              }
              $('#viewReportButton').on('click', function () {
                $('#reportModal').modal('show');
                $.ajax({
                  url: "${pageContext.request.contextPath}/api/trainee/getLabDetailReport?labId=" + labId,
                  method: 'GET',
                  success: function (data) {
                    renderTheDataInModel(data);
                    $('#reportModal').modal('show');
                  },
                  error: function (xhr, status, error) {
                    console.error('Error fetching report details:', error);
                  }
                });
              });

            })

          </script>
          <script>
            function renderTheDataInModel(data) {
              $('#reportModalBody').empty();

              $.each(data.detailReportPayloads, function (index, payload) {
                var cardContainer = $('<div class="card text-primary mb-1"></div>');
                var cardHeader = $('<div class="card-header border border-light rounded d-flex justify-content-between" onclick="toggleCardBody(this)"></div>');

                var textBlackDiv = $('<div class="text-black d-flex justify-content-between align-items-center"></div>');
                textBlackDiv.append('<div style="width: 652px; display: flex;">Q.' + (index + 1) + '<span class="text-sm"><span class="truncate-text">' + payload.questionDescription + '</span></span></div>');

                var collapseIconDiv = $('<div class="collapse-icon p-1"></div>');
                collapseIconDiv.append('<i class="fas fa-chevron-down"></i>');

                if (payload.labSubmitQueries && payload.labSubmitQueries.length > 0) {
                  textBlackDiv.append(collapseIconDiv);
                }
                if (payload.traineeCurrentQuestionPoints !== undefined && payload.questionPoints !== undefined) {
                  textBlackDiv.append('<div>' + payload.traineeCurrentQuestionPoints + ' / ' + payload.questionPoints + '</div>');
                }
                cardHeader.append(textBlackDiv);
                if (payload.labSubmitQueries && payload.labSubmitQueries.length > 0) {
                  var collapseCardBody = $('<div class="collapse show" style="display: none;" id="collapseCardBody">' +
                    '<div class="card-body"><table class="table"><thead><tr><th scope="col">S No.</th><th scope="col">Query Submitted</th><th scope="col">Status</th></tr></thead><tbody></tbody></table></div>' +
                    '</div>');

                  $.each(payload.labSubmitQueries, function (i, query) {
                    collapseCardBody.find('tbody').append('<tr><td>' + (i + 1) + '</td><td>' + query.querySubmit + '</td><td>' + query.status + '</td></tr>');
                  });
                  cardContainer.append(cardHeader).append(collapseCardBody);
                } else {
                  cardContainer.append(cardHeader);
                }

                $('#reportModalBody').append(cardContainer);
              });
            }

            function toggleCardBody(cardHeader) {
              var cardBody = cardHeader.nextElementSibling;
              var icon = cardHeader.querySelector('.collapse-icon i');
              if (cardBody != null) {
                if (cardBody.style.display === 'none') {
                  cardBody.style.display = 'block';
                  icon.classList.remove('fa-chevron-down');
                  icon.classList.add('fa-chevron-up');
                } else {
                  cardBody.style.display = 'none';
                  icon.classList.remove('fa-chevron-up');
                  icon.classList.add('fa-chevron-down');
                }
              }
            }

          </script>
          <script>
            $(document).ready(function () {
              const queryString = window.location.search;
              const urlParams = new URLSearchParams(queryString);
              const labId = urlParams.get('labId')
              $('#loadingSpinner').show();
              $.ajax({
                type: "GET",
                url: "${pageContext.request.contextPath}/api/trainee/getLabReport?labId=" + labId,
                contentType: "application/json",
                success: function (data) {
                  $('#loadingSpinner').hide();
                  $("#totalPoints").append(data.obtainedPoint + " / ")
                  $("#totalPoints").append(data.totalPoints)
                  $("#obtainedPoint").append(data.obtainedPoint)
                  let array = mergeQuestion(data.labInfo.questionStatusList, data.recordLabAttemptList)
                  renderTable(array);
                  let questionCount = countQuestion(data.labInfo.questionStatusList);
                  renderStatusCount(questionCount);
                  $("#reportTable").DataTable()
                },
                error: function (xhr, status, error) {
                  $('#loadingSpinner').hide();
                  console.error("Error:", error);
                },
              });
              function mergeQuestion(array1, array2) {
                const mergedArray = [];

                array1.forEach(obj1 => {
                  const matchingObj = array2.find(obj2 => obj1.questionId === obj2.questionId);
                  if (matchingObj) {
                    mergedArray.push({ ...obj1, ...matchingObj });
                  } else {
                    mergedArray.push({ ...obj1 });
                  }
                });
                return mergedArray;
              }
              function countQuestion(data) {
                const statusCount = {
                  INCORRECT: 0,
                  UNATTEMPTED: 0,
                  CORRECT: 0,
                  SKIP: 0
                };

                data.forEach(item => {
                  if (item.status === 'INCORRECT') {
                    statusCount.INCORRECT++;
                  } else if (item.status === 'UNATTEMPTED') {
                    statusCount.UNATTEMPTED++;
                  } else if (item.status === 'CORRECT') {
                    statusCount.CORRECT++;
                  }
                  //else if (item.status === 'SKIP') {
                  // statusCount.SKIP++;
                  // }
                });

                return statusCount;
              }
              function renderTable(data) {
                const tableData = document.getElementById('tableData');
                data.forEach((item, index) => {
                  const row = document.createElement('tr');

                  const serialNumberCell = document.createElement('td');
                  serialNumberCell.textContent = index + 1;
                  row.appendChild(serialNumberCell);

                  ['status', 'incorrectAttempt'].forEach(key => {
                    const cell = document.createElement('td');
                    if (status == "UNATTEMPTED") {
                      cell.textContent = item[key] || 'N/A';
                    } else {
                      cell.textContent = item[key] || '0';
                    }
                    row.appendChild(cell);
                  });
                  // const labSubmitQueriesCell = document.createElement('td');
                  // labSubmitQueriesCell.textContent = (item.status === 'UNATTEMPTED' || item.status ===  'SKIP') ? 'NA' : item.labSubmitQueries[0].querySubmit;
                  // row.appendChild(labSubmitQueriesCell);
                  tableData.appendChild(row);
                });
              }
              function renderStatusCount(statusCounts) {
                $("#correctQuestion").append(statusCounts.CORRECT)
                $("#inCorrectQuestion").append(statusCounts.INCORRECT)
                $("#unAttemptedQuestion").append(statusCounts.UNATTEMPTED)
                //   $("#skipQuestion").append(statusCounts.SKIP)  
                renderChart(statusCounts);
              }
              // statusCounts=statusCounts;  

              function renderChart(statusCounts) {
                const ctx = document.getElementById('statusChart').getContext('2d');
                new Chart(ctx, {
                  type: 'bar',
                  data: {
                    //   labels: ['Correct', 'Incorrect', 'Unattempted', 'Skip'],
                    labels: ['Correct', 'Incorrect', 'Unattempted'],
                    datasets: [{
                      label: 'Question Status',
                      data: [statusCounts.CORRECT, statusCounts.INCORRECT, statusCounts.UNATTEMPTED],
                      backgroundColor: [
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        //   'rgba(255, 206, 86, 0.2)'
                      ],
                      borderColor: [
                        'rgba(75, 192, 192, 1)',
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        //   'rgba(255, 206, 86, 1)'
                      ],
                      borderWidth: 1
                    }]
                  },
                  options: {
                    scales: {
                      y: {
                        beginAtZero: true
                      }
                    }
                  }
                });
                // statusChart.canvas.parentNode.style.width = '200px';
                // statusChart.canvas.parentNode.style.height = '50px';
              }
            })
          </script>