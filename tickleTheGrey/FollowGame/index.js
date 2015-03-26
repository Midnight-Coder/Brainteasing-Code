var canvas = document.getElementById('canvas'),
    ctx = canvas.getContext('2d'),
    $canvas = $('#canvas'),
    canvasOffset = $canvas.offset(),
    offsetX = canvasOffset.left,
    offsetY = canvasOffset.top,

    //The jewel box
    //Start position of the box
    boxOffsetX = 10,
    boxOffsetY = 20,
    colorGrey = "#B7B6BA",
    colorGreen = '#6EB58C',
    //speed controls the level: easy:100, medium:50, hard:20
    speed = 50,
    //dimension control the difficulty too: easy:60, medium: 40, hard: 20
    boxDims = 60,   //square box
    //direction: 1 --> increment 0 --> decrement
    direction = {
        x: 1,
        y: 1
    },

    //The game state
    $over = $('#tips'),
    $light = $('#light'),
    $fade = $('#fade'),
    isGameOn = false,
    intervalID;

startGame();

function randomMovement(){
    var x,y,
        increment = 2;
    return interval = setInterval(function() {

        if(boxOffsetX >= (canvas.width-boxDims)) {
            direction['x'] = -1;
        }
        else if(boxOffsetX <=(3)){
            direction['x'] = 1;
        }
        else if(boxOffsetY >= (canvas.height-boxDims)){
            direction['y'] = -1;
        }
        else if(boxOffsetY <=(boxDims)){
            direction['y'] = 1;
        }
        x = boxOffsetX + direction['x'];
        y = boxOffsetY + direction['y'];

        draw(x, y, boxDims, colorGreen);
    }, speed);
}
function gameOver(){
    $over.text('Game Over');
    $light.show();
    $fade.show();
    $canvas.unbind('mousemove', handleMouseMovement);
    ctx.clearRect(0,0,canvas.width,canvas.height);
    isGameOn = false;
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



