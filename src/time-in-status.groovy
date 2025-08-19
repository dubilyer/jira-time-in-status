import java.time.*
import java.time.format.DateTimeFormatter

// === CONFIG ===
def TARGET_STATUS = "[PUT YOUR STATUS HERE]"
def jql = "PUT YOUR QUERY HERE"

// === HELPERS ===
def parseDate(String s) {
    return OffsetDateTime.parse(
        s.replaceFirst(/([+-]\d{2})(\d{2})$/, '$1:$2'),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    )
}

def formatDuration(long seconds) {
    long days = seconds.intdiv(86400)
    long hours = (seconds % 86400).intdiv(3600)
    long mins = (seconds % 3600).intdiv(60)
    long secs = seconds % 60
    return "${days}d ${hours}h ${mins}m ${secs}s"
}

// === FETCH ISSUES WITH HAPI (only keys) ===
def keys = Issues.search(jql).collect { it.key }

def now = OffsetDateTime.now()
def results = [:]

// === CALCULATE DURATIONS ===
keys.each { key ->
    // Use REST to get full issue with changelog
    def issue = get("/rest/api/2/issue/${key}")
        .queryString("expand", "changelog")
        .asObject(Map)
        .body

    def histories = issue.changelog?.histories ?: []
    def totalSeconds = 0L
    OffsetDateTime lastEntered = null

    histories.sort { it.created }.each { h ->
        def created = parseDate(h.created)
        h.items.each { item ->
            if (item.field == "status") {
                if (item.toString == TARGET_STATUS) {
                    lastEntered = created
                } else if (item.fromString == TARGET_STATUS && lastEntered != null) {
                    totalSeconds += Duration.between(lastEntered, created).seconds
                    lastEntered = null
                }
            }
        }
    }

    if (lastEntered != null) {
        totalSeconds += Duration.between(lastEntered, now).seconds
    }

    results[key] = totalSeconds
}

// === OUTPUT (Aligned Markdown Table) ===
def sorted = results.sort { -it.value }

def header1 = "Issue Key"
def header2 = "Total Time in ${TARGET_STATUS}"

def rows = sorted.collect { k,v -> [k, formatDuration(v)] }

int col1Width = ([header1] + rows.collect { it[0] }).collect { it.size() }.max()
int col2Width = ([header2] + rows.collect { it[1] }).collect { it.size() }.max()

def sb = new StringBuilder()
sb << "| ${header1.padRight(col1Width)} | ${header2.padRight(col2Width)} |\n"
sb << "|-${'-' * col1Width}-|-${'-' * col2Width}-|\n"

rows.each { r ->
    sb << "| ${r[0].padRight(col1Width)} | ${r[1].padRight(col2Width)} |\n"
}

println sb.toString()
