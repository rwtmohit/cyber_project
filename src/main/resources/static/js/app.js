function el(id){return document.getElementById(id)}
function renderRiskLevel(label){
  const box = el('riskBox')
  if(!box) return
  if(!label){
    box.className = 'risk-box hidden'
    box.textContent = ''
    return
  }
  const normalized = String(label).trim().toLowerCase()
  let levelClass = 'unknown'
  if(normalized.includes('safe')) levelClass = 'safe'
  else if(normalized.includes('moderate')) levelClass = 'moderate'
  else if(normalized.includes('high')) levelClass = 'high'
  box.className = `risk-box ${levelClass} animate`
  box.textContent = label
}
function showResult(obj){
  const pre = el('resultArea')
  if(typeof obj === 'string'){
    renderRiskLevel('')
    pre.textContent = obj
  } else {
    const risk = obj.risk_level || obj.riskLevel || obj.verdict || ''
    renderRiskLevel(risk)
    const output = {...obj}
    if(output.risk_level) delete output.risk_level
    if(output.riskLevel) delete output.riskLevel
    pre.textContent = JSON.stringify(output, null, 2)
  }
}
async function analyze(){
  const url = el('urlInput').value.trim()
  if(!url){showResult('Please enter a URL');return}
  renderRiskLevel('')
  showResult('Analyzing...')
  try{
    const res = await fetch(`/api/analyze?url=${encodeURIComponent(url)}`)
    const data = await res.json()
    showResult(data)
  }catch(e){showResult('Error: '+e.message)}
}
async function checkUrl(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
  renderRiskLevel('')
  showResult('Checking URL...')
  try{
    const res = await fetch('/api/url/check',{
      method:'POST',headers:{'Content-Type':'application/json'},
      body: JSON.stringify({url})
    })
    const data = await res.json()
    showResult(data)
  }catch(e){showResult('Error: '+e.message)}
}
async function googleSafe(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
  renderRiskLevel('')
  showResult('Checking Google Safe Browsing...')
  try{
    const res = await fetch(`/api/url/google-safeservice?url=${encodeURIComponent(url)}`)
    const txt = await res.text()
    showResult(txt)
  }catch(e){showResult('Error: '+e.message)}
}
async function virustotal(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
  renderRiskLevel('')
  showResult('Checking VirusTotal...')
  try{
    const res = await fetch(`/api/url/virustotal?url=${encodeURIComponent(url)}`)
    const txt = await res.text()
    showResult(txt)
  }catch(e){showResult('Error: '+e.message)}
}
async function whois(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter domain');return}
  // try to extract hostname if user passed full URL
  try{ const u = new URL(url); var domain = u.hostname }catch(e){ domain = url }
  renderRiskLevel('')
  showResult('Fetching whois...')
  try{
    const res = await fetch(`/api/whois?domain=${encodeURIComponent(domain)}`)
    const data = await res.json()
    showResult(data)
  }catch(e){showResult('Error: '+e.message)}
}
function downloadReport(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
  window.location = `/api/download-report?url=${encodeURIComponent(url)}`
}

document.addEventListener('DOMContentLoaded',()=>{
  el('analyzeBtn').addEventListener('click',analyze)
  const checkButton = el('checkBtn')
  if(checkButton) checkButton.addEventListener('click',checkUrl)
  el('googleBtn').addEventListener('click',googleSafe)
  el('vtBtn').addEventListener('click',virustotal)
  el('whoisBtn').addEventListener('click',whois)
  el('downloadBtn').addEventListener('click',downloadReport)
})
