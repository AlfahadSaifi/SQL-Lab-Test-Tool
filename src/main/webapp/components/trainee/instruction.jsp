<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<div class="container-fluid ml-3 mr-3">
  <div class="shadow-sm p-3  mb-0 bg-white rounded">
    <hr>
    <div class="flexCenter">
      <b>Instructions<span class="mr-5" style="float:right;"></span></b>
    </div>
    <hr>
    <h6>General Guidelines</h6>
    <ul>
      <li>This practice lab consists of SQL query-based questions.</li>
      <li>Read each question carefully and construct your SQL query accordingly.</li>
      <li>You will see one question at a time along with the SQL schema provided.</li>
      <li>Compose your SQL query in the text box provided.</li>
      <li>Ensure the query syntax is accurate before submission.</li>
      <li>Make sure you have a stable internet connection.</li>
    </ul>
    <hr>
    <div class="d-flex justify-content-around">
      <p class="watermark">Nuclues Software</p>
      <p class="watermark">Nuclues Software</p>
      <p class="watermark">Nuclues Software</p>
    </div>
    <h6>Taking the practice lab</h6>
    <ul>
      <li>Write your SQL query in the text box that best satisfies the question's requirements.</li>
      <li>Each question comprises of different point.</li>
      <li>Once you have written your query for a question, you can proceed to the next question.</li>
      <li>If you complete the practice lab , you may click the "Submit" button to end the practice.</li>
    </ul>
    <hr>
    <h6>Submitting the practice lab</h6>
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
      <form action="${pageContext.request.contextPath}/trainee/labStart?labId=${labId}" method="post">
        <div class="form-check">
          <input class="form-check-input" type="checkbox"  id="flexCheckDefault"  required>
          <label class="form-check-label" for="flexCheckDefault">
            I declare that I have read all the instructions.
          </label>
        </div>
        <button  class="btn btn-success btn-sm" id="startLabBtn" disabled>Start Lab</button>
        <hr>
      </form>   
      <hr>
  </div>
</div>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    const checkbox = document.getElementById('flexCheckDefault');
    const startLabBtn = document.getElementById('startLabBtn');

    checkbox.addEventListener('change', function() {
      startLabBtn.disabled = !checkbox.checked;
    });
  });
</script>