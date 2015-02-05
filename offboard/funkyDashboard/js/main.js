var socket = io.connect();

var running = true;
var datasets = {};

$.ajax("datasets.json").done(function(datasetArray) {
  $('ul.tabs').tabs();

  for (var i = 0; i < datasetArray.length; i++) {
    var dataset = datasetArray[i];
    var card = createCard(dataset.id, dataset.name, createDataset(dataset.id, dataset.type));
    document.getElementById("card-holder-" + (i % 2)).appendChild(card);
    if (dataset.type == "graph") {
      datasets[dataset.id] = {
        type: "graph",
        data: [],
        lastX: 0,
        element: "#" + dataset.id,
        running: true
      }
    } else if (dataset.type == "boolean") {
      datasets[dataset.id] = {
        type: "boolean",
        element: "#" + dataset.id,
        running: true
      }
    } else if (dataset.type == "log") {
      datasets[dataset.id] = {
        type: "log",
        element: "#" + dataset.id,
        running: true
      }
    } else if (dataset.type == "string") {
      datasets[dataset.id] = {
        type: "string",
        element: "#" + dataset.id,
        running: true
      }
    }
  }

  display("all");

  socket.on('data-update', function(msg) {
    if (running) {
      msg = JSON.parse(msg);
      var dataset = datasets[msg.type];
      if (dataset.running) {
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
        } else if (dataset.type == "log") {
          $(dataset.element).append('<p>' + msg.value + '</p>');
          $(dataset.element).scrollTop($(dataset.element).prop("scrollHeight"));
        } else if (dataset.type == "string") {
          $(dataset.element).text(msg.value);
        }
      }
    }
  });
});

function togglePause() {
  if (running) {
    running = false;
    $("#pauseToggle").text("Resume all the things");
  } else {
    running = true;
    $("#pauseToggle").text("Pause all the things");
  }
}

function togglePauseCard(id) {
  datasets[id].running = !datasets[id].running;
  if (datasets[id].running) {
    $("#toggle-pause-" + id).text("Pause this card");
  } else {
    $("#toggle-pause-" + id).text("Resume this card");
  }
}

function display(datasetName) {
  $.ajax("./groups/" + datasetName + ".json").done(function(toDisplay) {
    var cards = document.getElementsByClassName('card');
    for (var i = 0; i < cards.length; i++) {
      cards[i].style.display = 'none';
    }

    for (var i = 0; i < toDisplay.length; i++) {
      document.getElementById("card-" + toDisplay[i]).style.display = 'block';
    }
  });
}
