var serverip = "123.123.123.123";
var myWindow = window.open("http://"+serverip+":4567/");
const config = { attributes: true, childList: true, subtree: true };
const callback = function(mutationsList, observer) {
    msgContainer = document.querySelectorAll("[class='_1RAno message-in focusable-list-item']")[document.querySelectorAll("[class='_1RAno message-in focusable-list-item']").length-1];
    msgContent = msgContainer.getElementsByClassName("_1VzZY selectable-text invisible-space copyable-text")[0].innerText;
    msgTimestamp = new Date().getTime();
    console.log(msgContent);
    try {
    msgParts = JSON.parse(msgContent);
    if (msgParts.length == 2) {
        if (Number.isInteger(msgParts[0]) || (Array.isArray(msgParts[0]) && !msgParts[0].some(i => !Number.isInteger(i))) ) {
            //if (msgParts[1].length == 6 && /^[0-9A-F]{6}$/i.test(msgParts[1])) {
                console.log("Matches");
                myWindow.location.href="http://"+serverip+":4567/basic/"+msgParts[1]+"?timestamp="+msgTimestamp+"&ids="+JSON.stringify(msgParts[0]);
    	    //}      
    	    //else { console.log("Fail2"); }
        } else {
        console.log("Fail1");
        }
    }
    
    } catch (e) {}

};

const observer = new MutationObserver(callback);
observer.observe(document, config);
