# Jira Time in Status (ScriptRunner Groovy)

📊 ScriptRunner Groovy script for Jira Cloud and Data Center to calculate **total time in status**.
Uses changelogs, handles multiple transitions in/out of a status, and outputs a clean aligned Markdown table.

---

## ✨ Features

* Works with **Jira Cloud (HAPI)** and **Jira Data Center (REST)**.
* Calculates **total time in a status**, not just last entry.
* Handles multiple entries/exits into the same status.
* Generates **aligned Markdown tables** for easy reporting.
* Configurable with JQL and target status.

---

## 🚀 Usage

1. Open **ScriptRunner Console** in Jira.
2. Copy the script from this repository.
3. Adjust the configuration at the top of the script:

   ```groovy
   def TARGET_STATUS = "READY FOR QA"
   def jql = "project = EP AND status = '${TARGET_STATUS}'"
   ```
4. Run the script.
5. Copy the Markdown output into Jira, Confluence, or GitHub.

---

## 📋 Example Output

```markdown
| Issue Key | Total Time in READY FOR QA |
|-----------|----------------------------|
| EP-50864  | 20d 22h 36m 17s            |
| EP-48990  | 15d  5h  0m 47s            |
| EP-51432  |  1d 22h 55m  7s            |
```

---

## 📖 How It Works

* Issues are fetched using **HAPI** (Cloud) or REST API (Data Center).
* Each changelog entry is inspected for status changes.
* Every period an issue spends in the target status is measured.
* Durations are accumulated to calculate the total.
* If the issue is **still in the status**, time is counted up to **now**.

---

## 🛠 Requirements

* Jira Cloud or Jira Data Center
* [ScriptRunner](https://marketplace.atlassian.com/apps/6820/scriptrunner-for-jira) installed
* Groovy runtime (provided by ScriptRunner)

---

## 🔑 Keywords

`jira` · `jira-cloud` · `jira-dc` · `jira-groovy` · `scriptrunner` · `jira-scriptrunner` · `hapi` · `jira-automation` · `jira-reports` · `time-in-status` · `workflow-metrics`

---

## 🤝 Contributing

Contributions are welcome!
If you improve formatting, extend functionality (e.g., multiple statuses, CSV export), or optimize performance, please submit a PR.

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

⭐ If you find this useful, please **star** the repo and share it with your team!
