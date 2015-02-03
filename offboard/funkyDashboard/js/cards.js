function createCard(id, name, datasetContent) {
  var inner = '<div id="card-' + id + '" class="card">' +
                '<div class="card-image">' + datasetContent + '</div>' +
                '<div class="card-content activator">' +
                  '<span class="card-title grey-text text-darken-4">' + name + '</span>' +
                '</div>' +
              '</div>'

  var element = document.createElement("div");
  element.className = "col s12 m6";
  element.innerHTML = inner;

  return element;
}

function createGraph(id) {
  return '<div id="' + id + '" class="card-graph"></div>';
}

function createBoolean(id) {
  return '<div class="row"><div id="' + id + '" class="boolean-state-flip-container center-block"><div class="boolean-state-flipper"><p class="boolean-state-on"><i></i></p><p class="boolean-state-off"><i></i></p></div></div></div>';
}

function createLog(id) {
  return '<div class="row"><div id="' + id + '" class="log"</div></div>';
}

function createDataset(id, type) {
  if (type == "graph") {
    return createGraph(id);
  } else if (type == "boolean") {
    return createBoolean(id);
  } else if (type == "log") {
    return createLog(id);
  }
}
