var canvas = document.getElementById('canvas'),
    ctx = canvas.getContext('2d'),
    $canvas = $('#canvas'),
    canvasOffset = $canvas.offset(),
    offsetX = canvasOffset.left,
    offsetY = canvasOffset.top,
    //The jewel box
    boxDims = 40,   //square box
    boxOffsetX = 10,
    boxOffsetY = 20,
    colorGrey = "#B7B6BA",
    colorGreen = '#6EB58C',
    //The game state
    $over = $('#over'),
    isGameOn = false,
    $light = $('#light'),
    $fade = $('#fade');


//grey box to start with
startGame();

$canvas.mousemove(handleMouseMovement)

function randomMovement(){
    var x, y;
    x = Math.floor(Math.random() * (2)) + boxOffsetX;
    y = Math.floor(Math.random() * (2)) + boxOffsetX;
    setInterval(function() {

        draw(boxOffsetX, boxOffsetY, boxDims, colorGreen);
    },1000);
}
function gameOver(){
    $over.text('Game Over');
    $light.show();
    $fade.show();

    draw(boxOffsetX, boxOffsetY, boxDims, colorGrey);
    console.log('game over');
}
function startGame(){
    console.log('starting')
    $light.hide();
    $fade.hide();
    $over.text('Move mouse on the box');
    isGameOn = false;
    boxOffsetX = 10;
    boxOffsetY = 20;
    draw(boxOffsetX, boxOffsetY, boxDims, colorGrey);
}

function handleMouseMovement(e){
    e.preventDefault();
    e.stopPropagation();
    window.s = e;
    mouseX = e.offsetX;
    mouseY = e.offsetY;

    if(!isGameOn){
        if(isOnBox(mouseX, mouseY)){
            draw(boxOffsetX, boxOffsetY, boxDims, colorGreen);
            isGameOn = true;
            console.log('let it roll');
            $over.text('Score:' + Math.random()*100);
        }
    }
    else if(!isOnBox(mouseX, mouseY)){
        gameOver();
    }
    else {
        randomMovement();
    }
}

function isOnBox (mouseX, mouseY){
    if(mouseX >= boxOffsetX && mouseX <= (boxOffsetX + boxDims)) {
        if(mouseY >= boxOffsetY && mouseY <= (boxOffsetY + boxDims)){
            return true;
        }
    }
    return false;
}

function draw(x, y, boxDims, color){
    boxOffsetX = x;
    boxOffsetY = y;
    ctx.clearRect(0,0,canvas.width,canvas.height);
    ctx.fillStyle = color;
    ctx.fillRect(boxOffsetX, boxOffsetY, boxDims, boxDims);
}



