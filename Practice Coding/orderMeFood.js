var _ = require('underscore');

  
  (function test(){
    var peopleAndPreferences = 
      ["Rohit likes pizza", 
      "Rohit likes cake",
      "Chuck likes pizza", 
      "Chuck likes burgers",
      "Bob likes tacos"];

    var foodsAvailable = 
      ["Pizza is avaiable",
      "Burgers are avaible"];

    var expectedSolution = 
      ["Rohit eats pizza",
      "Chuck eats pizza, burgers"];
    
    var foodMatches = yourSolution(peopleAndPreferences, foodsAvailable);
    console.log(foodMatches);
    console.log(expectedSolution);
    if (isListEqual(expectedSolution, foodMatches)){
      console.log("Everything works!");
    } else {
      console.log("Hmm, something's not right...");      
    }
  }());

  function yourSolution(peopleAndPreferences, foodsAvailable){
    var orderedFood = {},
        isAvailable = menu(foodsAvailable),
        orderPlaced = [],
        name,
        placeHolderArray,
        sentence;
    peopleAndPreferences.forEach(function(i){
      sentence = i.split(' ');
      if(isAvailable(sentence[2])){
        if(orderedFood[sentence[0]]){
          orderedFood[sentence[0]].push(sentence[2]);
        }
        else{
          //Overrides the toString for the array
          placeHolderArray = new Array(sentence[2]);
          placeHolderArray.toString = function(){
                return this.join(', ');
          }
          orderedFood[sentence[0]] = placeHolderArray;
        }
      }
    });    
    for(var name in orderedFood){
      orderPlaced.push(name + ' eats ' + orderedFood[name]);
    }
    return orderPlaced;
  }

  //Checks if an item someone likes is present in the menu
  function menu(foodsAvailable){
      foodsAvailable = stripNonessentials(foodsAvailable);
      return function(item){
        if(foodsAvailable.indexOf(item) !== -1){
          return true;
        }
        else{
          return false;
        }
      }
  }

  //input 2 arrays
  //output boolean
  //checks for equality in 2 arrays
  function isListEqual(x,y){
    if(x.length !== y.length){
      return false;
    }
    if(_.difference(x,y).length === 0 && _.difference(y,x).length === 0){
      return true;
    }
    return false;
  }

  //input: array of strings
  //output: array of strings; wherein each string is 1 word long
  //Strips everything after the first word for each string
  function stripNonessentials(foodsAvailable){
    return foodsAvailable.map(function(sentence){
      return sentence.substring(0, sentence.indexOf(' ')).toLowerCase();
    });
  }