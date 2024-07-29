<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
      <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ page isELIgnored="false" %>
          <div class="container-fluid ml-3 mr-3">
            <div class="shadow-sm p-3  mb-0 bg-white rounded">
              <hr>
              <div class="row">
                <div class="col-4 bold">Start Date: ${labTest.startDate}</div>
                <div class="col-2 bold text-center ">Test Name: <span class="text-green">${labTest.labTestName}</span>
                </div>
                <div class="col-4 bold">End Date: ${labTest.endDate} </div>
                <div class="col-2 bold flexCenter">Test Duration: ${labTest.duration} Mins</div>
              </div>
              <hr>
              <div class="flexCenter">
                <b>Instructions<span class="mr-5" style="float:right;"></span></b>
              </div>
              <h6>General Guidelines</h6>
              <ul>
                <li>This exam consists of SQL query-based questions.</li>
                <li>Read each question carefully and construct your SQL query accordingly.</li>
                <li>You will see one question at a time along with the SQL schema provided.</li>
                <li>Compose your SQL query in the text box provided.</li>
                <li>Ensure the query syntax is accurate before submission.</li>
                <li>There is a time limit for the entire exam. </li>
                <li>Make sure you have a stable internet connection.</li>
                <li>You are not allowed to switch the tab and exit from the full screen while test is running. </li>
                <li>After two switches of tab, it will be considered as malpractice. </li>
                <li>Negative Marking Factor: <b>${labTest.negativeMarkingFactor} % </b></li>
              </ul>
              <hr>
              <div class="d-flex justify-content-around">
                <p class="watermark">Nuclues Software</p>
                <p class="watermark">Nuclues Software</p>
                <p class="watermark">Nuclues Software</p>
              </div>
              <h6>Taking the Test</h6>
              <ul>
                <li>Write your SQL query in the text box that best satisfies the question's requirements. </li>
                <li>You must construct the query within the specified time for the entire exam. </li>
                <li>Each question comprises of different point. </li>
                <li>Once you have written your query for a question, you can proceed to the next question. </li>
                <li>Complete all questions within the given time limit for the entire exam. </li>
                <li>If you complete the exam before the time limit, you may click the "Submit" button to end the exam.
                </li>
                <li>Lab Test only be conducted in full-screen mode. </li>
                <li>There is fixed number of attempts for questions. </li>
              </ul>
              <hr>
              <h6>Submitting the Test</h6>
              <ul>
                <li> When you have finished answering all questions or the time limit has elapsed, click the "Submit"
                  button.</li>
                <li>The Quiz will be auto submitted when time is elapsed.</li>
                <li>Wait for the system to process your submission.</li>
                <li>After successful submission the report will be displayed on the screen.</li>
              </ul>

              <p>Good luck with your SQL exam!</p>
              <div class="d-flex justify-content-around">
                <p class="watermark">Nuclues Software</p>
                <p class="watermark">Nuclues Software</p>
                <p class="watermark">Nuclues Software</p>
              </div>
              <hr>
              <form action="${pageContext.request.contextPath}/trainee/labTestStart?labTestId=${labTest.labTestId}"
                method="post">
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" id="flexCheckDefault" required>
                  <label class="form-check-label" for="flexCheckDefault">
                    I declare that I have read all the instructions.
                  </label>
                </div>
                <button class="btn btn-success btn-sm" id="startLabBtn" disabled>Start Lab Test</button>
                <hr>
              </form>
              <hr>
            </div>
          </div>
          <script>
            document.addEventListener('DOMContentLoaded', function () {
              let elem = document.documentElement;
              elem.requestFullscreen();
              const checkbox = document.getElementById('flexCheckDefault');
              const startLabBtn = document.getElementById('startLabBtn');

              checkbox.addEventListener('change', function () {
                startLabBtn.disabled = !checkbox.checked;
              });
            });
          </script>