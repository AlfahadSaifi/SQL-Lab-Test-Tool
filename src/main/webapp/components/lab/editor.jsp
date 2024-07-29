<div class="container -fluid m-3 mt-5">
<!-- Left Sidebar -->
<div class="leftsidebar">
    <h1>SQL LABS</h1>
    <ul>
        <li><a href="#">LABORATORY 1</a></li>
        <li><a href="#">LABORATORY 2</a></li>
        <li><a href="#">LABORATORY 3</a></li>
        <li><a href="#">LABORATORY 4</a></li>
        <li><a href="#">LABORATORY 5</a></li>
        <li><a href="#">LABORATORY 6</a></li>
        <li><a href="#">LABORATORY 7</a></li>
    </ul>
</div>

<!-- Compiler -->
<div class="compiler">
    <!-- Editor -->
    <div class="editor">
        <form action="lab?id=${question.questionId}" method="post">
            <label>SQL Editor</label>
            <button type="submit" class="btn">RUN</button>
            <h1 class="qd">${question.questionDescription}</h1>
            <textarea id="sql-code" required autocomplete="off" name="query"
                placeholder="Write Your Code Here...">${query}</textarea>
        </form>
    </div>
    <!-- Console -->
    <div class="console">
        <label>Output</label>
        ${output}
        <div class="console1">
            <table class="table">
                <c:if test="${not empty outputList}">
                    <c:forEach var="e" items="${outputList}">
                        <tr>
                            <c:forEach var="item" items="${e}">
                                <th>${item}</th>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </c:if>
            </table>
        </div>
    </div>
</div>
</div>

<!-- Right Sidebar -->
<div class="rightsidebar" id="right-sidebar">
    <c:forEach var="e" items="${questionList}">
        <button class="toggle-btn">Question ${e.questionId}</button>
        <p>
            <a href="${pageContext.request.contextPath}/lab?id=${e.questionId}">${e.questionDescription}</a>
        </p>
    </c:forEach>
</div>
</div>

<h2>Trainee Access</h2>
</div>
</div>
