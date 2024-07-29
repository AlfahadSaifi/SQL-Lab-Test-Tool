<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false"%>
<%@ page import = "lab.dto.questionBank.QuestionDto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Questions</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
    <script>
        // Define an array to hold question descriptions and queries
        var questionData = [];

        <c:forEach var="question" items="${questions}">
            var questionDescription = '${question.questionDescription.replaceAll("\\n", " ").replace("'", "\\'").trim()}';
            var query = '${question.questionAnswer.replaceAll("\\n", " ").replace("'", "\\'").trim()}';
            var question = {
                questionId: '${question.questionId}',
                questionDescription: questionDescription,
                query: query
            };
            questionData.push(question);
        </c:forEach>

        $(document).ready(function() {
            var selectedQuestions = new Set();

            $('#questionsTable').DataTable();

            // Handle checkbox change event
            $('#questionsTable tbody').on('change', 'input[type="checkbox"]', function() {
                var questionId = $(this).val().trim(); // Trim whitespace
                if ($(this).is(':checked') && questionId !== "") {
                    // Add selected question ID to the set
                    selectedQuestions.add(questionId);
                } else {
                    // Remove unselected question ID from the set
                    selectedQuestions.delete(questionId);
                }
            });

            // Submit form with selected question IDs
            $('form').submit(function() {
                var selectedQuestionsArray = Array.from(selectedQuestions);
                $('#selectedQuestions').val('');
                $('#selectedQuestions').val(selectedQuestionsArray.join(','));
            });
        });

        function openPreview(questionId) {
            var question = questionData.find(function(item) {
                return item.questionId === questionId;
            });

            if (question) {
                var popupWindow = window.open("", "Preview", "width=600,height=400");
                popupWindow.document.write("<html><head><title>Question Preview</title></head><body>");
                popupWindow.document.write("<h2>Question ID:</h2><p>" + question.questionId + "</p>");
                popupWindow.document.write("<h2>Question Description:</h2><p>" + question.questionDescription + "</p>");
                popupWindow.document.write("<h2>Query:</h2><p>" + question.query + "</p>");
                popupWindow.document.write("</body></html>");
                popupWindow.document.close();
            } else {
                alert("Question not found.");
            }
        }
    </script>
</head>
<body>
    <div>
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
    </div>
    <form action="${pageContext.request.contextPath}/admin/addSelectedQuestions?id=${id}" method="post">
        <table id="questionsTable" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>Select</th>
                    <th>Question Description</th>
                    <th>Preview</th>
                    <th>Question Points</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td><input type="checkbox" name="selectedQuestions" value="${question.questionId}"></td>
                        <td>${question.questionDescription}</td>
                        <td><a href="#" onclick="openPreview('${question.questionId}'); return false;">Preview</a></td>
                        <td>${question.questionPoints}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <!-- Hidden input field to hold the selected question IDs -->
        <input type="hidden" id="selectedQuestions" name="selectedQuestions">
        <!-- Submit button -->
        <input type="submit" value="Add Selected Questions">
    </form>
</body>
</html>
