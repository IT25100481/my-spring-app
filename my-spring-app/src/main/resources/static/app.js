document.getElementById('btn').addEventListener('click', () => {
  fetch('/api/hello')
    .then(res => res.json())
    .then(data => {
      document.getElementById('result').textContent = data.message;
    });
});