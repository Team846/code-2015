var socket = io.connect();

$.ajax("datasets.json").done(function(datasetArray) {
  var datasets = {}
  for (var i = 0; i < datasetArray.length; i++) {
    var dataset = datasetArray[i];
    var card = createCard(dataset.name, dataset.detailedInfo, createDataset(dataset.id, dataset.type));
    document.getElementById("card-holder").appendChild(card);
    if (dataset.type == "graph") {
      datasets[dataset.id] = {
        type: "graph",
        data: [],
        lastX: 0,
        element: "#" + dataset.id
      }
    } else if (dataset.type == "boolean") {
      datasets[dataset.id] = {
        type: "boolean",
        element: "#" + dataset.id
      }
    }
  }

  socket.on('data-update', function(msg) {
    msg = JSON.parse(msg);
    var dataset = datasets[msg.type];

    if (dataset.type == "graph") {
      dataset.data.push([dataset.lastX, msg.value]);
      if (dataset.data.length > 50) {
        dataset.data.shift();
      }

      dataset.lastX += 1;
      $.plot($(dataset.element), [ dataset.data ], {
        series: {
          lines: {
            fill: true,
            fillColor: "rgba(255, 0, 0, 0.5)"
          },
          color: "rgba(255, 0, 0, 0.5)"
        }, yaxis: { max: 5 }
      });
    } else if (dataset.type == "boolean") {
      if (msg.value) {
        $(dataset.element).removeClass("flipped");
      } else {
        $(dataset.element).addClass("flipped");
      }
    }
  });
});
