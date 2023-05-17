<!--./script.js-->
function loadJson() {
    fetch('http://localhost:8080/data.json')
        .then(response => response.json())
        .then(data => {
            const jsonTable = document.getElementById('json-table');
            const headerRow = document.getElementById('header-row');
            const tbody = jsonTable.getElementsByTagName('tbody')[0];

            // Clear existing table headers and rows
            headerRow.innerHTML = '';
            tbody.innerHTML = '';

            // headers
            const headers = Object.keys(data[0]);
            headers.forEach(header => {
                const th = document.createElement('th');
                th.textContent = header;
                headerRow.appendChild(th);
            });

            // rows
            data.forEach(item => {
                const row = document.createElement('tr');

                headers.forEach(header => {
                    const cell = document.createElement('td');
                    cell.textContent = item[header];
                    row.appendChild(cell);
                });
                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error:', error));
    console.log("data.json reloaded")
}
loadJson();

function reloadP() {
    fetch("http://localhost:8080/reload")
        .then(loadJson)
        .catch(error => console.error('Error:', error));
}
