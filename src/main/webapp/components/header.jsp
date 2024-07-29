<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NSBT-BRD</title>
    <link rel="icon" href="../assets/images/nucleus-software.svg" type="image/icon type">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.1.0/dist/sweetalert2.min.css">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script>
    <%@include file="/js/time.js" %>
    </script>

    <style type='text/css'>
        html,body{
          /* font-family: "Times New Roman";
          background-size: cover;
          height: 100%;
          overflow: auto;
          color: black; */
         /* background-color:hsla(166, 23%, 89%, 0.836); */
        }
        .centered-form{
        display:flex;
        justify-content:center;
        align-items:center;
        height:100vh;
        }


        .side-card {
          border: 1px solid #ccc;
          border-radius: 5px;
          padding: 10px;
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
          background-color: #fff;
          max-width: 300px;
          overflow-y: auto;
          height: 500px; /* Adjust the height as needed */
        }

        .scrollable-content {
          padding: 10px;
        }

        /* Optionally, you can style the paragraph text */
        .scrollable-content p {
          margin: 0;
        }

/* Output table content css */
        .output_table_content{
          justify-content: space-evenly;
          max-width: 700px;
          height:200px;
          padding:10px;
          overflow-x:scroll;
          overflow-y:scroll;          
        }
        .watermark {
                  position: relative;
                  bottom: 0;
                  left: 0;
                  transform-origin: bottom left;
                  transform: rotate(-45deg);
                  color: black; /* Adjust the color as needed */
                  font-size: 12px; /* Adjust the font size as needed */
                  opacity: 0.4; /* Adjust the opacity as needed */
                  z-index: 9999; /* Adjust the z-index to ensure the watermark appears on top */
                }
/*
        #login {
            max-width: 500px;
            margin: 0 auto;
            background-color: rgb(232, 243, 253);
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
        }

        */
    </style>



</head>