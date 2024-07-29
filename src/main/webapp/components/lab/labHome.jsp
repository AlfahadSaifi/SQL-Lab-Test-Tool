<%-- (-sks)this page is redesigned with jstl switch case inorder to handle different cases to show question answer
    submission for page --%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ page isELIgnored="false" %>
            <%@ page import="javax.servlet.http.HttpSession" %>
                <html>

                <head>
                    <script>
                        document.addEventListener("DOMContentLoaded", function () {
                            var timerElement = document.getElementById('timer');
                            var submitButton = document.getElementById('submitBtn');
                            var timerInterval;
                            var endTimeKey = 'timerEndTime';
                            // Assuming you have a userId available
                            var userId = '${userId}';
                            // Use userId to create unique identifiers
                            var testId = 'test_' + userId;
                            var labId = 'lab_' + userId;
                            var storedEndTime = sessionStorage.getItem(endTimeKey);
                            if (storedEndTime) {
                                // If storedEndTime exists in sessionStorage, resume the timer
                                startTimer(parseInt(storedEndTime));
                            } else {
                                // If storedEndTime does not exist, calculate endTime and start the timer
                                var durationInMinutes = test.duration;
                                var systemTime = Date.now();
                                var endTime = systemTime + durationInMinutes * 60000;
                                startTimer(endTime);
                            }
                            function startTimer(endTime) {
                                timerInterval = setInterval(function () {
                                    var currentTime = Date.now();
                                    var remainingTime = endTime - currentTime;
                                    if (remainingTime <= 0) {
                                        clearInterval(timerInterval);
                                        timerElement.textContent = "Time's up!";
                                        // Clear the stored end time from sessionStorage
                                        sessionStorage.removeItem(endTimeKey);
                                        // Auto-submit the test when the timer ends
                                        alert("Test automatically submitted.");
                                        window.location.href = '${pageContext.request.contextPath}/trainee/submitLab?testId=' + testId;
                                    } else {
                                        var minutes = Math.floor(remainingTime / 60000);
                                        var seconds = Math.floor((remainingTime % 60000) / 1000);
                                        timerElement.textContent = "Time Remaining: " + minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
                                        // Store the end time in sessionStorage
                                        sessionStorage.setItem(endTimeKey, endTime.toString());
                                    }
                                }, 1000);
                            }
                            submitButton.addEventListener('click', function (event) {
                                var confirmed = confirm('Are you sure want to Final Submit?');
                                if (confirmed) {
                                    clearInterval(timerInterval);
                                    // Clear the stored end time from sessionStorage when the test is manually submitted
                                    sessionStorage.removeItem(endTimeKey);
                                    alert("Test manually submitted.");
                                    // Add your logic for submitting the test here
                                } else {
                                    // Prevent the default form submission if the user cancels
                                    event.preventDefault();
                                }
                            });
                        });
                    </script>
                </head>

                <body>
                    <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
                        <h3 class="text-center mt-8 pt-5">SQL LAB</h3>
                        <div class="container-fluid m-8 shadow-sm p-3 mb-5 bg-white rounded">
                            <div id="form1" class="m-5">
                                <div id="timer"></div>
                                <h3>${userId}</h3>
                                <c:choose>
                                    <c:when test="${not empty questionList}">
                                        <form action="" method="post">
                                            <h5 class="text-primary">${question.questionDescription}</h5>
                                            <div class="mb-3">
                                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"
                                                    name="query" placeholder="${question.questionDescription}" required
                                                    autocomplete="off">${query}</textarea>
                                            </div>
                                            <c:if test="${query!=''}">
                                                <div class="mb-3">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </c:if>
                                        </form>
                                    </c:when>
                                    <c:when test="${not empty testQuestionList}">
                                        <form action="" method="post">
                                            <h5 class="text-primary">${testQuestion.questionDescription}</h5>
                                            <div class="mb-3">
                                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"
                                                    name="query" placeholder="${testQuestion.questionDescription}"
                                                    required autocomplete="off">${query}</textarea>
                                            </div>
                                            <c:if test="${query!=''}">
                                                <div class"mb-3">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </c:if>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <form action="" method="post">
                                            <h5 class="text-primary">${Question.questionDescription}</h5>
                                            <div class="mb-3">
                                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"
                                                    name="query" placeholder="${Question.questionDescription}" required
                                                    autocomplete="off">${query}</textarea>
                                            </div>
                                            <c:if test="${query!=''}">
                                                <div class"mb-3">
                                                    <button type="submit" class="btn btn-primary">Submit</button>
                                                </div>
                                            </c:if>
                                        </form>
                                    </c:otherwise>

                                </c:choose>
                            </div>
                        </div>
                        <div class="container-fluid m-8 shadow-sm p-3 mb-5 bg-white rounded">
                            <h3>Result:</h3>
                            <div class=" shadow-sm p-3 mb-5 rounded alert alert-warning pt-6" role="alert">
                                ${output}
                            </div>
                            <c:if test="${not empty outputList}">
                                <div class="output_table_content">
                                    <table class="table">
                                        <c:forEach var="e" items="${outputList}">
                                            <tr border="1">
                                                <c:forEach var="item" items="${e}">
                                                    <th>${item}</th>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </c:if>
                        </div>
                    </div>
                    <c:if test="${not empty questionList}">
                        <div class="side-card">
                            <a href="${pageContext.request.contextPath}/trainee/download/${labId}">Lab Reference</a><br>
                            <h6 class="text-center mt-8 pt-5">LAB QUESTION</h6>
                            <c:forEach var="e" items="${questionList}">
                                <div class="scrollable-content">
                                    <a
                                        href="${pageContext.request.contextPath}/trainee/?id=${labId}&qid=${e.questionId}">${e.questionDescription}</a>
                                </div>
                            </c:forEach>
                            <br>
                            <div>
                                <a class="btn btn-danger"
                                    href="${pageContext.request.contextPath}/trainee/submitLab?id=${labId}"
                                    onclick="return confirm('Are you sure want to Final Submit?')">SUBMIT LAB</a>
                            </div>
                        </div>
                    </c:if>
                    <%-- (-sks) now the side bar will appear if the candidate clicks on one of the given Test links and
                        that test has questions --%>
                        <c:if test="${not empty testQuestionList}">
                            <div class="side-card">
                                <a href="${pageContext.request.contextPath}/trainee/download/${testId}">Test
                                    Reference</a><br>
                                <h6 class="text-center mt-8 pt-5">TEST QUESTIONS</h6>

                                <c:forEach var="e" items="${testQuestionList}">
                                    <div class="scrollable-content">
                                        <a
                                            href="${pageContext.request.contextPath}/trainee/?testId=${testId}&testQid=${e.questionId}">${e.questionDescription}</a>
                                    </div>
                                </c:forEach>
                                <br>
                                <h3>${test.duration}</h3>
                                <div>

                                    <a class="btn btn-danger" id="submitBtn"
                                        href="${pageContext.request.contextPath}/trainee/submitLab?testId=${labId}">SUBMIT
                                        Test</a>
                                </div>
                            </div>

                        </c:if>

                        </div>
                </body>

                </html>