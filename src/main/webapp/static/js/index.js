window.onload = function() {
  repositoryDetailsByUserName('conorheffron');
};

function repositoryDetailsByUserName(username) {
  fetch('/get-repo-detail?username=' + username)
  .then(Response => Response.json())
  .then(repoDetails => {
    renderRepoDetails(repoDetails);
})};

function renderRepoDetails(val){
    let output = document.getElementById("repo-output");
    let html = '';

    val.forEach((repo_row, index) =>{
        html += `<tr>
            <th scope="row">${index}</th>
            <td>${repo_row.name}</td>
            <td>${repo_row.description}</td>
            <td><a href=${repo_row.appHome} target="_blank">${repo_row.appHome}</a></td>
            <td><a href=${repo_row.repoUrl} target="_blank">${repo_row.fullName}</a></td>
            <td>${repo_row.topics}</td>
            </tr>
        `;

    })
    output.innerHTML = html;
}