function openNav() {
    document.getElementById("mySidebar").style.width = "350px";
    document.getElementById("main").style.marginRight = "350px";
}

function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginRight = "0";
}

function pay() {
    document.getElementById("payment-checkout").style.display = "none";
    document.getElementById("payment-paying").style.display = "block";
    window.setTimeout(() => {
        document.getElementById("payment-paying").style.display = "none";
        document.getElementById("payment-accepted").style.display = "block";
    }, 3000);
    window.setTimeout(() => {
        document.getElementById("payment-accepted").style.display = "none";
        document.getElementById("payment-orders").style.display = "block";
    }, 3000);

}