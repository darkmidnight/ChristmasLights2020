var serverip = "123.123.123.123";
var myWindow = window.open("http://"+serverip+":4567/");
const config = { attributes: true, childList: true, subtree: true };
const callback = function(mutationsList, observer) {
    msgContainer = document.querySelectorAll("[class='_1-FMR message-in focusable-list-item _7GVCb']")[document.querySelectorAll("[class='_1-FMR message-in focusable-list-item _7GVCb']").length-1];
    msgContent = msgContainer.getElementsByClassName("i0jNr selectable-text copyable-text")[0].innerText;
    msgTimestamp = new Date().getTime();
    console.log(msgContent);
    try {
    msgParts = JSON.parse(msgContent);
    if (msgParts.length == 2) {
        if (Number.isInteger(msgParts[0]) || (Array.isArray(msgParts[0]) && !msgParts[0].some(i => !Number.isInteger(i))) ) {
            console.log("Matches");
            myWindow.location.href="http://"+serverip+":4567/basic/"+msgParts[1]+"?timestamp="+msgTimestamp+"&ids="+JSON.stringify(msgParts[0]);
        } else {
        console.log("Fail1");
        }
    }
    
    } catch (e) {
        msgBits = msgContent.split(" ");
        if (['all', 'half', 'odd', 'even', 'split', 'bottom', 'top', 'alternate'].includes(msgBits[0].toLowerCase())) {
            match = false;
            
            theMode = msgBits.shift();
            msgBits.forEach(element => { if (['red','green','blue','white','orange','yellow','magenta','pink','cyan','black'].includes(element.toLowerCase())) { match = true; } });
            msgBits.forEach(element => { if (element.match(/[A-Fa-f0-9]{6}/)) { match = true; } });
            thePath = "";
            
            if (match) {
            msgBits.forEach(element => thePath = thePath + "/"+element.toLowerCase());
            console.log("Keyword Matches");
            myWindow.location.href="http://"+serverip+":4567/"+theMode.toLowerCase()+thePath+"?timestamp="+msgTimestamp;
            } 
            match = false;
        } else if (['led'].includes(msgBits[0].toLowerCase())) {
                    match = false;

            if (!isNaN(parseInt(msgBits[1]))) {
                                console.log("RAE "+parseInt(msgBits[1]));
            if (['red','green','blue','white','orange','yellow','magenta','pink','cyan','black'].includes(msgBits[2].toLowerCase())||msgBits[2].match(/[A-Fa-f0-9]{6}/)) { match = true; 
                                console.log("RAE "+msgBits[2]);
                                }
            
            }
            
            thePath = "";
            
            if (match) {
            msgBits.forEach(element => thePath = thePath + "/"+element.toLowerCase());
            console.log("Keyword Matches");
            myWindow.location.href="http://"+serverip+":4567/"+thePath+"?timestamp="+msgTimestamp;
            } 
            match = false;
        } else if (/^[Mm]orse [A-Za-z0-9 ]+$/.test(msgContent) && msgContent.length<32) {
            console.log("Morse Matches");
            myWindow.location.href="http://"+serverip+":4567/morse?morseStr="+msgContent.substr(6);
        }
    }

};

const observer = new MutationObserver(callback);
observer.observe(document, config);
