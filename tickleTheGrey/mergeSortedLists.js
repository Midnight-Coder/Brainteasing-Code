//merge n sorted arrays
function merge(){
    var metaList = new Array(arguments.length),
        metaIterators = new Array(arguments.length),
        mergedList,
        totalElements = 0,
        smallest, i;

    for(var i = 0; i < metaList.length; i++){
        //prevents argument leakage: https://github.com/petkaantonov/bluebird/wiki/Optimization-killers#32-leaking-arguments
        metaList[i] = arguments[i];
        //Evaluate the length of the mergedList
        totalElements += metaList[i].length;
        //Initialize the iterators of each sub array to 0
        metaIterators[i] = 0;
    }
    mergedList = new Array();

    while(totalElements-- > 0){
        smallest = metaList[0][metaIterators[0]];
        indexOfSmallest = 0;
        for(i = 1; i < metaIterators.length; i++){
            indexOfSubArray = metaIterators[i];
            if(metaList[i][indexOfSubArray] < smallest){
                smallest = metaList[i][indexOfSubArray];
                indexOfSmallest = i;
            }
        }
        mergedList.push(smallest);
        metaIterators[indexOfSmallest]++;
        if(metaIterators[indexOfSmallest] >= metaList[indexOfSmallest].length){
            //remove the sublist that has been completely processed and it's correcposnding iterator
            metaIterators.splice(indexOfSmallest, 1);
            metaList.splice(indexOfSmallest, 1);
        }
    }
    console.log(mergedList);
}
(function runner(){
    var a1 = [1],
        a2 = [6,10,22,45];
        a3 = [2,25];
    merge(a1, a2, a3);
})();