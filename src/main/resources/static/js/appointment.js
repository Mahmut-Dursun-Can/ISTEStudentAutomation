function selectTime(time) {
        var slots = document.querySelectorAll('.time-slot');
        slots.forEach(function(slot) {
            slot.classList.remove('selected');
        });
        document.getElementById('selectedTime').value = time;
        var selectedSlot = document.querySelector('.time-slot[onclick="selectTime(\'' + time + '\')"]');
        selectedSlot.classList.add('selected');
    }

      window.addEventListener("scroll", function () {
        const footer = document.getElementById("footer");
        const scrollPosition = window.scrollY + window.innerHeight;
        const pageHeight = document.documentElement.scrollHeight;

        if (scrollPosition >= pageHeight) {
          footer.style.display = "block";
        } else {
          footer.style.display = "none";
        }
      });