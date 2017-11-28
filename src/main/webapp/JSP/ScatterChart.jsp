<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable(<%= request.getParameter("detailArray")%>);

        var options = {
          title:<%= request.getParameter("title") %>,
          hAxis: {title: <%= request.getParameter("hAxis") %>, minValue: <%= request.getParameter("hAxisMin") %>, maxValue: <%= request.getParameter("hAxisMax") %>},
          vAxis: {title: <%= request.getParameter("vAxis") %>, minValue: <%= request.getParameter("vAxisMin") %>, maxValue: <%= request.getParameter("vAxisMax") %>},
          legend: 'none'
        };

        var chart = new google.visualization.ScatterChart(document.getElementById('chart_div'));

        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
    <div id="chart_div" style="width: 720px; height: 400px;"></div>
  </body>
</html>
