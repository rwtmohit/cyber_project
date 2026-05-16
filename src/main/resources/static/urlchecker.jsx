import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

export default function SketchFrontend() {
  const [screen, setScreen] = useState('landing');
  const [authMode, setAuthMode] = useState('signin');
  const [otp, setOtp] = useState('');
  // For URL analysis
  const [urlInput, setUrlInput] = useState('');
  const [analyzeResult, setAnalyzeResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const Landing = () => (
    <div className="min-h-screen flex flex-col bg-gradient-to-br from-slate-900 via-indigo-900 to-slate-950 text-white">
      <header className="flex justify-between items-center p-6 backdrop-blur-xl bg-white/10 border-b border-white/10">
        <h1 className="text-3xl font-extrabold tracking-wide bg-gradient-to-r from-cyan-400 to-purple-400 bg-clip-text text-transparent">URLChecker</h1>
        <div className="space-x-3">
          <Button className="border border-white/30 bg-white/10 text-white hover:bg-white/20" onClick={() => {setAuthMode('signin'); setScreen('auth')}}>Sign In</Button>
          <Button onClick={() => {setAuthMode('signup'); setScreen('auth')}}>Sign Up</Button>
        </div>
      </header>
      <main className="flex-1 flex flex-col items-center justify-center p-10 text-center gap-8 relative">
        <motion.div initial={{ y: 20, opacity: 0 }} animate={{ y: 0, opacity: 1 }} className="bg-white/10 backdrop-blur-2xl p-12 rounded-3xl shadow-2xl border border-white/20 max-w-2xl w-full">
          <h2 className="text-6xl font-extrabold mb-4 bg-gradient-to-r from-cyan-300 to-purple-400 bg-clip-text text-transparent">Welcome</h2>
          <p className="text-xl text-slate-300 mb-8">Paste your URL and instantly validate or analyze it</p>
          <div className="w-full max-w-lg mx-auto mb-6">
            <Input placeholder="paste url" className="h-16 text-lg rounded-2xl bg-white/20 border-white/20 text-white placeholder:text-slate-300 px-6" />
          </div>
          <Button size="lg" className="px-10 py-6 rounded-2xl text-lg font-semibold shadow-xl bg-gradient-to-r from-cyan-500 to-purple-600 hover:scale-105 transition-all" onClick={() => setScreen('home')}>Get Started</Button>
        </motion.div>
      </main>
    </div>
  );

  const Auth = () => (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-indigo-900 to-slate-950 text-white">
      <Card className="w-full max-w-md p-8 rounded-3xl shadow-2xl bg-white/10 backdrop-blur-2xl border border-white/20">
        <CardContent className="space-y-4">
          <h2 className="text-3xl font-extrabold text-center bg-gradient-to-r from-cyan-300 to-purple-400 bg-clip-text text-transparent">{authMode === 'signin' ? 'Sign In' : 'Sign Up'}</h2>
          <Input placeholder="Email" className="h-14 rounded-2xl bg-white/20 border-white/20 text-white placeholder:text-slate-300" />
          <Input placeholder="Password" type="password" className="h-14 rounded-2xl bg-white/20 border-white/20 text-white placeholder:text-slate-300" />
          <Button className="w-full h-14 rounded-2xl bg-gradient-to-r from-cyan-500 to-purple-600 text-lg font-semibold" onClick={() => authMode === 'signup' ? setScreen('otp') : setScreen('home')}>
            Enter
          </Button>
        </CardContent>
      </Card>
    </div>
  );

  const OTPValidation = () => (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-indigo-900 to-slate-950 text-white">
      <Card className="w-full max-w-md p-8 rounded-3xl shadow-2xl bg-white/10 backdrop-blur-2xl border border-white/20">
        <CardContent className="space-y-4">
          <h2 className="text-3xl font-extrabold text-center bg-gradient-to-r from-cyan-300 to-purple-400 bg-clip-text text-transparent">OTP Verification</h2>
          <p className="text-center text-slate-300">Enter the OTP sent to your email</p>
          <Input value={otp} onChange={(e) => setOtp(e.target.value)} placeholder="Enter OTP" className="h-14 rounded-2xl bg-white/20 border-white/20 text-white placeholder:text-slate-300 text-center text-xl tracking-widest" />
          <Button className="w-full h-14 rounded-2xl bg-gradient-to-r from-cyan-500 to-purple-600 text-lg font-semibold" onClick={() => setScreen('home')}>
            Validate OTP
          </Button>
        </CardContent>
      </Card>
    </div>
  );

  const Home = () => (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-indigo-900 to-slate-950 text-white p-8">
      <div className="flex justify-between items-center mb-10">
        <h1 className="text-4xl font-extrabold bg-gradient-to-r from-cyan-300 to-purple-400 bg-clip-text text-transparent">Dashboard</h1>
        <div className="flex gap-4">
          <Button className="border border-white/30 bg-white/10 text-white hover:bg-white/20">History</Button>
          <Button className="border border-white/30 bg-white/10 text-white hover:bg-white/20">Download</Button>
          <Button className="border border-red-400/40 bg-red-500/20 text-white hover:bg-red-500/30" onClick={() => setScreen('landing')}>Logout</Button>
        </div>
      </div>

      <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }}>
        <Card className="rounded-3xl shadow-2xl p-10 bg-white/10 backdrop-blur-xl border border-white/20 max-w-4xl mx-auto">
          <CardContent className="space-y-8">
            <h2 className="text-2xl font-bold text-center">Analyze URL</h2>
            <Input
              placeholder="Enter URL here..."
              className="h-16 rounded-2xl bg-white/20 border-white/20 text-white placeholder:text-slate-300 text-lg px-6"
              value={urlInput}
              onChange={e => setUrlInput(e.target.value)}
              disabled={loading}
            />
            <Button
              className="w-full h-14 rounded-2xl bg-gradient-to-r from-cyan-500 to-purple-600 text-lg font-semibold"
              onClick={async () => {
                setLoading(true);
                setError('');
                setAnalyzeResult(null);
                try {
                  const res = await fetch(`/api/analyze?url=${encodeURIComponent(urlInput)}`);
                  if (!res.ok) throw new Error('Failed to analyze URL');
                  const data = await res.json();
                  setAnalyzeResult(data);
                } catch (err) {
                  setError(err.message || 'Unknown error');
                } finally {
                  setLoading(false);
                }
              }}
              disabled={loading || !urlInput}
            >
              {loading ? 'Analyzing...' : 'Analyze'}
            </Button>

            <div className="rounded-2xl bg-slate-800/60 border border-white/10 p-8 min-h-[220px]">
              <h3 className="text-xl font-semibold mb-4 text-cyan-300">Output</h3>
              <div className="space-y-3 text-slate-300">
                {error && <p className="text-red-400">Error: {error}</p>}
                {analyzeResult ? (
                  <pre className="whitespace-pre-wrap break-all text-sm bg-slate-900/40 p-4 rounded-xl border border-slate-700 overflow-x-auto max-h-80">{JSON.stringify(analyzeResult, null, 2)}</pre>
                ) : !error && <p className="text-slate-400">No result yet. Enter a URL and click Analyze.</p>}
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }}>
      {screen === 'landing' && <Landing />}
      {screen === 'auth' && <Auth />}
      {screen === 'otp' && authMode === 'signup' && <OTPValidation />}
      {screen === 'home' && <Home />}
    </motion.div>
  );
}
