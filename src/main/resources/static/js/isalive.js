
function createOrb(s, x, y, r) {
 return {
  circle: s.circle(x,y,r),
  active: false,
  processing: false,
  tx: 0,
  ty: 0,
 }
}

function reportMouseOver(z0) {
 if (z0.active) {
  z0.active = false
 } else {
  z0.active = true
  getTarget(z0)
  loopIn(z0)
 }
}

function loopOut(z0) {
 z0.circle.animate({
  r: 20,
  cx: 400
 }, 2000, mina.easein, function() {
  loopIn(z0);
 });
}

function loopIn(z0) {
 if (!z0.active) {
  return
 }
 z0.circle.animate({
  r: 0,
  cx: 600
 }, 1000, mina.easein, function() {
  loopOut(z0);
 });
}

function getTarget(z0) {
 var tronk = {
  "x": z0.tx,
  "y": z0.ty,
  "fill" : z0.fill,
  "tx": 0,
  "ty": 0
 }
 $.ajax({
  type: "POST",
  contentType : 'application/json; charset=utf-8',
  url: "/ajaxGetTarget",
  data: JSON.stringify(tronk),
   success: function(msg) {
            z0.circle.attr({
              fill: msg.fill,
              stroke: "#f00",
              strokeWidth: 1
            });
    },
    error: function(xhr) {
        //Do Something to handle error
    }
 });
}