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
    $over = $('#tips'),
    $light = $('#light'),
    $fade = $('#fade'),
    isGameOn = false,
    intervalID;

startGame();

function randomMovement(){
    var x = Math.floor(Math.random() * (2)) + boxOffsetX,
        y = Math.floor(Math.random() * (2)) + boxOffsetX;

    return interval = setInterval(function() {
        draw(x, y, boxDims, colorGreen);
    },1000);
}
function gameOver(){
    $over.text('Game Over');
    $light.show();
    $fade.show();
    $canvas.unbind('mousemove', handleMouseMovement);
    draw(boxOffsetX, boxOffsetY, boxDims, 'red');
    isGameOn = false;
    console.log('game over');
}
function startGame(){
    $light.hide();
    $fade.hide();
    $over.text('Move mouse on the box');
    boxOffsetX = 10;
    boxOffsetY = 20;
    //grey box to start with
    draw(boxOffsetX, boxOffsetY, boxDims, colorGrey);
    $canvas.mousemove(handleMouseMovement);
}

function handleMouseMovement(e){
    e.preventDefault();
    e.stopPropagation();

    if(!isGameOn){
        if(isOnBox(e.offsetX, e.offsetY)){
            draw(boxOffsetX, boxOffsetY, boxDims, colorGreen);
            isGameOn = true;
            console.log('let it roll');
            $over.text('Don\'t let the box fly away');
            intervalID = randomMovement();
        }
    }
    else if(!isOnBox(e.offsetX, e.offsetY)){
        clearInterval(intervalID);
        gameOver();
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



