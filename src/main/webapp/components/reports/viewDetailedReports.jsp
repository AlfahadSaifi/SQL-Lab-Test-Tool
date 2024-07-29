<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ page isELIgnored="false" %>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <div class="row m-0">
      <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
      </div>
      <div id="testReportSection" class="col-9 m-0 p-2" style="width: 80%; overflow-y: scroll; height: 540px;">
        <c:if test="${not empty successMessage}">
          <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <div class="container shadow p-1 mb-2 bg-white rounded">
          <div class="mt-1">
            <div class="row p-2">
              <div class="">
                <div class="card mb-1">
                  <div class="card-header">
                    <b>Trainee Information</b>
                  </div>
                  <div class="card-body">
                    <div class="row">
                      <div class="col-4 m-0 d-flex gap-2" style="flex-direction: column">
                        <span><b>Trainee ID:</b> ${traineeId}</span>
                        <span><b>Trainee Name:</b> ${traineeName}</span>
                        <span><b>Result:</b> ${gotScore}/${totalScore}
                          <c:choose>
                            <c:when test="${result eq 'PASS'}">
                              <span class="badge bg-success">${result}</span>
                            </c:when>
                            <c:when test="${result eq 'FAIL'}">
                              <span class="badge bg-danger">${result}</span>
                            </c:when>
                          </c:choose>
                        </span>
                      </div>
                      <div class="col-4 m-0 d-flex gap-2" style="flex-direction: column">
                        <span><b>Batch Code:</b> ${batchCode}</span>
                        <span><b>Lab Test Name:</b> ${labName}</span>
                        <span><b>Total Question:</b> ${totalLabQuestions}</span>
                      </div>
                      <div class="col-4 m-0 d-flex gap-2" style="flex-direction: column">
                        <span><b>Total Skip Ques.:</b> ${totalSkipQuestions}</span>
                        <span><b>Total Correct Ques.:</b>
                          ${totalCorrectQuestions}</span>
                        <span><b>Total Incorrect Ques.:</b>
                          ${totalIncorrectQuestions}</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
          <div class="container">
            <h4 class="text-center pb-1">Detailed Lab Test Report</h4>
            <div class="m-2" style="display: flex; flex-direction: row-reverse;">
              <div class="input-group w-25">
                <input type="text" id="searchInput" onkeyup="searchCards()" class="form-control" placeholder="Search.."
                  aria-label="Search" aria-describedby="search-addon">
                <button class="btn btn-outline-secondary" type="button" id="search-addon">
                  <i class="fas fa-search"></i>
                </button>
              </div>
            </div>
            <%! int count=1; %>
              <c:forEach var="e" items="${detailReportPayloads}">
                <div id="cardContainer">
                  <div class="card text-primary mb-1">
                    <div class="card-header border border-light rounded   d-flex justify-content-between"
                      onclick="toggleCardBody(this)">
                      <div class="text-black d-flex justify-content-between align-items-center">
                        <div>
                          Q.<%= count %>
                            <span class="text-sm">
                              <span class="truncate-text">${e.questionDescription}</span>
                            </span>
                        </div>
                        <c:if test="${not empty e.labSubmitQueries}">
                          <div class="collapse-icon p-1">
                            <i class="fas fa-chevron-down"></i>
                          </div>
                        </c:if>
                      </div>

                      <div>
                        <c:choose>
                          <c:when test="${not empty e.labSubmitQueries}">
                            ${e.traineeCurrentQuestionPoints} / ${e.questionPoints}
                          </c:when>
                          <c:otherwise>
                            <div class="max-content-width">
                              <div class="flexCenter">
                                <span class="">${e.traineeCurrentQuestionPoints} /
                                  ${e.questionPoints}</span>
                                <div class="skipQues">Skip</div>
                              </div>
                            </div>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </div>
                    <c:if test="${not empty e.labSubmitQueries}">
                      <div class="collapse show " style="display: none;" id="collapseCardBody">
                        <div class="card-body">
                          <table class="table">
                            <thead>
                              <tr>
                                <th scope="col">S No.</th>
                                <th scope="col">Query Submitted</th>
                                <th scope="col">Status</th>
                              </tr>
                            </thead>
                            <tbody>
                              <c:forEach var="lq" items="${e.labSubmitQueries}" varStatus="loop">
                                <tr>
                                  <td>${loop.index + 1}</td>
                                  <td>${lq.querySubmit}</td>
                                  <td>${lq.status}</td>
                                </tr>
                              </c:forEach>
                            </tbody>
                          </table>
                        </div>

                      </div>
                    </c:if>
                  </div>
                </div>
                <% count++; %>
              </c:forEach>
              <% count=1; %>
          </div>
          <ul class="pagination justify-content-center" id="pagination"></ul>

        </div>
      </div>
    </div>
    <script>
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

      // Function to filter cards based on search input
      function searchCards() {
        var input = document.getElementById('searchInput').value.toUpperCase();
        var cards = document.querySelectorAll('#cardContainer .card');

        cards.forEach(function (card) {
          var text = card.innerText.toUpperCase();
          if (text.indexOf(input) > -1) {
            card.style.display = '';
          } else {
            card.style.display = 'none';
          }
        });
      }

      function displayCards(start, end) {
        var cards = document.querySelectorAll('#cardContainer .card');

        cards.forEach(function (card, index) {
          if (index >= start && index < end) {
            card.style.display = '';
          } else {
            card.style.display = 'none';
          }
        });
      }
      var currentPage = 0;
      var cardsPerPage = 5; // Number of cards per page
      var cards = document.querySelectorAll('#cardContainer .card');
      var pageCount = Math.ceil(cards.length / cardsPerPage);

      // Function to go to the previous page
      function goPrevious() {
        if (currentPage > 0) {
          currentPage--;
          updatePage();
        }
      }

      // Function to go to the next page
      function goNext() {
        if (currentPage < pageCount - 1) {
          currentPage++;
          updatePage();
        }
      }

      // Function to update the displayed page
      function updatePage() {
        var start = currentPage * cardsPerPage;
        var end = start + cardsPerPage;
        displayCards(start, end);
        updatePagination();
      }

      // Function to update pagination links and buttons
      function updatePagination() {
        var pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        // Create "Previous" button
        var prevButton = document.createElement('li');
        prevButton.classList.add('page-item');
        var prevLink = document.createElement('a');
        prevLink.classList.add('page-link');
        prevLink.href = '#';
        prevLink.textContent = 'Previous';
        prevLink.onclick = goPrevious;
        prevButton.appendChild(prevLink);
        pagination.appendChild(prevButton);

        // Create pagination links
        for (var i = 1; i <= pageCount; i++) {
          var li = document.createElement('li');
          li.classList.add('page-item');
          var a = document.createElement('a');
          a.classList.add('page-link');
          a.href = '#';
          a.textContent = i;
          a.onclick = function () {
            currentPage = parseInt(this.textContent) - 1;
            updatePage();
          };
          if (currentPage === i - 1) { // Highlight active page
            li.classList.add('active');
          }
          li.appendChild(a);
          pagination.appendChild(li);
        }

        // Create "Next" button
        var nextButton = document.createElement('li');
        nextButton.classList.add('page-item');
        var nextLink = document.createElement('a');
        nextLink.classList.add('page-link');
        nextLink.href = '#';
        nextLink.textContent = 'Next';
        nextLink.onclick = goNext;
        nextButton.appendChild(nextLink);
        pagination.appendChild(nextButton);
      }

      // Display initial page
      updatePage();



    </script>