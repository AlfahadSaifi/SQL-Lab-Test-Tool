function updateCurrentTime() {

    const currentTimeElement = document.getElementById('currentTime');
    const currentTime = new Date();
    const formattedTime = currentTime.toLocaleTimeString('en-US').toUpperCase();
    currentTimeElement.textContent = formattedTime;
}

updateCurrentTime();

setInterval(updateCurrentTime, 1000);