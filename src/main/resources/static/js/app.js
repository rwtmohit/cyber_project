function el(id){return document.getElementById(id)}
function showResult(obj){
  const pre = el('resultArea')
  if(typeof obj === 'string') pre.textContent = obj
  else pre.textContent = JSON.stringify(obj,null,2)
}
async function analyze(){
  const url = el('urlInput').value.trim()
  if(!url){showResult('Please enter a URL');return}
  showResult('Analyzing...')
  try{
    const res = await fetch(`/api/analyze?url=${encodeURIComponent(url)}`)
    const data = await res.json()
    showResult(data)
  }catch(e){showResult('Error: '+e.message)}
}
async function checkUrl(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
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
  showResult('Checking Google Safe Browsing...')
  try{
    const res = await fetch(`/api/url/google-safeservice?url=${encodeURIComponent(url)}`)
    const txt = await res.text()
    showResult(txt)
  }catch(e){showResult('Error: '+e.message)}
}
async function virustotal(){
  const url = el('urlInput').value.trim(); if(!url){showResult('Enter URL');return}
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
  el('checkBtn').addEventListener('click',checkUrl)
  el('googleBtn').addEventListener('click',googleSafe)
  el('vtBtn').addEventListener('click',virustotal)
  el('whoisBtn').addEventListener('click',whois)
  el('downloadBtn').addEventListener('click',downloadReport)
})
