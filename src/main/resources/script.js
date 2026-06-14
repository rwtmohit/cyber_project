// Helper to grab elements quickly
function el(id) {
    return document.getElementById(id);
}

// 1. Theme Logic (Light / Dark Mode)
const themeKey = 'cyberTheme';

function applyTheme(theme) {
    const useLight = theme === 'light';
    document.body.classList.toggle('light', useLight);
    el('themeToggle').textContent = useLight ? '🌙' : '☀️';
    localStorage.setItem(themeKey, theme);
}

function toggleTheme() {
    const nextTheme = document.body.classList.contains('light') ? 'dark' : 'light';
    applyTheme(nextTheme);
}

function initializeTheme() {
    const savedTheme = localStorage.getItem(themeKey) || 'dark';
    applyTheme(savedTheme);
}

// 2. Help Button Logic
function showHelp() {
    alert('🚨 URL Scanner Help 🚨\n\n- Paste a link and hit Enter.\n- Wait for the security scan.\n- Download a PDF report once complete.\n- Toggle the theme button for Light/Dark mode.');
}

// 10-Phase Dynamic Color Generator
function getScoreColor(score) {
    if (score <= 10) return '#7f1d1d'; 
    if (score <= 20) return '#b91c1c'; 
    if (score <= 30) return '#ef4444'; 
    if (score <= 40) return '#ea580c'; 
    if (score <= 50) return '#f97316'; 
    if (score <= 60) return '#f59e0b'; 
    if (score <= 70) return '#eab308'; 
    if (score <= 80) return '#84cc16'; 
    if (score <= 90) return '#22c55e'; 
    return '#05742c';                  
}

// 3. Score Animation Magic 🪄 (Terminal Decryption Scramble)
function animateScore(targetScore) {
    const scoreEl = el("reportScore");
    const hackerChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%&*";
    let current = 0;
    const duration = 1800; 
    const interval = 30; 
    const step = targetScore / (duration / interval);

    if (targetScore === 0) {
        scoreEl.textContent = "0 / 100";
        scoreEl.style.color = getScoreColor(0);
        scoreEl.style.textShadow = `0 0 20px ${getScoreColor(0)}66`; 
        return;
    }

    const timer = setInterval(() => {
        current += step;
        
        const dynamicColor = getScoreColor(Math.floor(current));
        scoreEl.style.color = dynamicColor;
        scoreEl.style.textShadow = `0 0 20px ${dynamicColor}66`;

        if (current >= targetScore) {
            current = targetScore;
            clearInterval(timer);
            scoreEl.textContent = `${Math.floor(current)} / 100`;
        } else {
            let randomChar1 = hackerChars[Math.floor(Math.random() * hackerChars.length)];
            let randomChar2 = hackerChars[Math.floor(Math.random() * hackerChars.length)];
            scoreEl.textContent = `${Math.floor(current)}${randomChar1}${randomChar2} / 100`;
        }
        
    }, interval);
}

// 4. UI Updater Logic
function showResult(data) {
    const reportCard = el('reportCard');
    const downloadBtn = el('downloadBtn');
    
    if (reportCard) {
        reportCard.classList.remove('hidden');
        try { reportCard.scrollIntoView({ behavior: 'smooth', block: 'start' }); } catch (e) {}
    }
    
    if (downloadBtn) {
        downloadBtn.classList.remove('hidden'); 
    }
    
    renderReport(data);
    animateScore(data.totalScore || 0); 
}

// 5. Render Real Data to the HTML
function renderReport(data) {
    const verdict = data.verdict || "Unknown";
    const score = data.totalScore || 0;

    el("reportUrl").textContent = `🌐 ${data.url || "Unknown URL"}`;
    el("reportRisk").textContent = score >= 75 ? "Low Risk Profile" : score >= 40 ? "Moderate Risk Detected" : "High Risk Target";

    const status = el("reportStatus");
    status.className = "status"; 

    if (/safe|clean/i.test(verdict) || score >= 75) {
        status.classList.add("safe");
        status.innerHTML = "🟢 SECURE TARGET";
    } else if (/suspicious|warning/i.test(verdict) || score >= 40) {
        status.classList.add("suspicious");
        status.innerHTML = "🟡 SUSPICIOUS";
    } else {
        status.classList.add("danger");
        status.innerHTML = "🔴 DANGEROUS";
    }

    const ssl = data.sslReport || {};
    const dns = data.dnsReport || {};

    el("sslStatus").textContent = ssl.valid ? "Valid" : "Invalid/Expired";
    el("dnsStatus").textContent = dns.error ? "Anomalies Found" : "Healthy";
    el("malwareStatus").textContent = data.malicious > 0 ? `${data.malicious} Engines Flagged` : "Clean";
    el("phishingStatus").textContent = data.phishing ? "Impersonation Detected" : "Not Found";
    el("headersStatus").textContent = `${data.headerScore || 0} / 100`;

    el("ipAddress").textContent = ssl.host || "N/A";
    el("domainValid").textContent = ssl.endDate || "N/A";
    el("issuerName").textContent = ssl.issuer || "N/A";
    el("domainAge").textContent = ssl.startDate || "N/A";
}

// 6. REAL BACKEND API FETCH LOGIC
async function fetchVirusTotalData(url) {
    try {
        const res = await fetch(`/api/url/virustotal?url=${encodeURIComponent(url)}`);
        if (!res.ok) return null;
        return await res.json();
    } catch (e) {
        console.error("VT fetch error:", e);
        return null;
    }
}

// THE HOLLYWOOD HACKING SEQUENCE ENGINE
async function analyze() {
    const urlInput = el('urlInput');
    const analyzeBtn = el('analyzeBtn');
    const url = urlInput.value.trim();
    
    // Smooth Error Handling 
    if (!url) { 
        const originalText = analyzeBtn.textContent;
        analyzeBtn.textContent = '❌ No URL Found !!!';
        analyzeBtn.style.background = '#ef4444';
        analyzeBtn.style.boxShadow = '0 0 20px rgba(239, 68, 68, 0.5)';
        
        setTimeout(() => {
            analyzeBtn.textContent = originalText;
            analyzeBtn.style.background = ''; 
            analyzeBtn.style.boxShadow = '';
        }, 2000);
        
        urlInput.focus();
        return; 
    }
    
    analyzeBtn.disabled = true;
    el('reportCard').classList.add('hidden');
    el('downloadBtn').classList.add('hidden');

    // Laser Animation Initialization
    const searchBox = el('urlInput').closest('.glass-search-box');
    if (searchBox && !el('activeLaser')) {
        searchBox.insertAdjacentHTML('afterbegin', '<div id="activeLaser" class="laser-line"></div>');
    }

    try {
        // Step 1: Start the backend requests immediately
        const analyzeFetch = fetch(`/api/analyze?url=${encodeURIComponent(url)}`).then(res => {
            if (!res.ok) throw new Error('Analyze request failed');
            return res.json();
        });
        const vtFetch = fetchVirusTotalData(url);

        // Step 2: Run the sexy UI animation while waiting for your server
        const steps = [
            "Resolving Target IP...", 
            "Analyzing SSL Handshake...", 
            "Querying Threat Databases...", 
            "Compiling Security Matrix..."
        ];
        
        for(let i = 0; i < steps.length; i++) {
            analyzeBtn.textContent = `⏳ ${steps[i]}`;
            await new Promise(r => setTimeout(r, 600)); 
        }
        
        analyzeBtn.textContent = '⏳ Finalizing...';

        // Step 3: Await the real data
        const data = await analyzeFetch;
        const vtData = await vtFetch;
        
        if (vtData) {
            data.malicious = vtData.malicious || 0;
            data.suspicious = vtData.suspicious || 0;
            data.harmless = vtData.harmless || 0;
            data.totalVendors = vtData.totalVendors || 0;
            data.riskPercentage = vtData.riskPercentage || 0;
            data.flaggedBy = vtData.flaggedBy || 0;
        }
        
        // Send actual data to the UI
        showResult(data);

    } catch (e) {
        alert('Backend Error: ' + e.message); 
    } finally {
        analyzeBtn.textContent = "Analyze"; 
        analyzeBtn.disabled = false;
        el('urlInput').value = ''; 

        // Kill the laser
        const laser = el('activeLaser');
        if (laser) laser.remove(); 
    }
}

// 7. REAL PDF DOWNLOAD LOGIC
function downloadReport() {
    const url = el('reportUrl').textContent.replace('🌐 ', '').trim();
    if (!url || url === "Waiting for target..." || url === "Unknown URL") { 
        alert('No valid URL to download a report for!'); 
        return; 
    }
    
    const btn = el('downloadBtn');
    const originalText = btn.textContent;
    btn.textContent = "⏳ Downloading...";
    
    window.location.href = `/api/download-report?url=${encodeURIComponent(url)}`;
    
    setTimeout(() => {
        btn.textContent = originalText;
    }, 2000);
}

// 8. Event Listeners 
document.addEventListener('DOMContentLoaded', function() {
    el('analyzeBtn').addEventListener('click', analyze);
    el('downloadBtn').addEventListener('click', downloadReport);
    el('themeToggle').addEventListener('click', toggleTheme);
    el('helpBtn').addEventListener('click', showHelp);
    
    el('urlInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); 
            el('analyzeBtn').click(); 
        }
    });

    initializeTheme();
});