//Emulates the Set difference property
//theta(n) n: |A| or |B|
function outStanding(a, b){
    var map = {};
    a.forEach(function(value){
        map[value] = (map[value])? map[value]+1 : 1;
    });
    b.forEach(function(value){
        map[value] = (map[value])? map[value]-1 : 1;
    });
    console.log(cleanMap(map));

}
function cleanMap(map){
    var beautifiedList = [];
    for(var prop in map){
        if(map[prop] == 0){
            delete map[prop];
        }
        else{
            beautifiedList.push(prop);
        }
    }
    return beautifiedList;
}

(function runner(){
    var a = [5,1,5,3,2,5],
        b = [1,2,3,5,5,6,7,30];
    outStanding(a, b);
})();
