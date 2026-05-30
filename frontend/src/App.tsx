import { useState, useEffect } from 'react';
import { Quiz } from './components/Quiz';
import type { QuizState } from './components/Quiz';
import { Results } from './components/Results';
import type { RecommendationResult } from './components/Results';
import styles from './App.module.scss';

// Resolved at build time via VITE_API_BASE_URL env var.
// Locally falls back to http://localhost:8080
const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

type ViewState = 'landing' | 'quiz' | 'loading' | 'results' | 'error';
type Theme = 'dark' | 'light';

function App() {
  const [view, setView] = useState<ViewState>('landing');
  const [recommendations, setRecommendations] = useState<RecommendationResult[]>([]);
  const [errorMessage, setErrorMessage] = useState('');

  // Theme: read from localStorage on mount, default to dark
  const [theme, setTheme] = useState<Theme>(() => {
    return (localStorage.getItem('carfind-theme') as Theme) || 'dark';
  });

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('carfind-theme', theme);
  }, [theme]);

  const toggleTheme = () => setTheme(prev => prev === 'dark' ? 'light' : 'dark');

  const handleQuizSubmit = async (quizData: QuizState) => {
    setView('loading');
    setErrorMessage('');

    try {
      const response = await fetch(`${API_BASE}/api/recommend`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(quizData),
      });

      if (!response.ok) {
        throw new Error(`API returned an error code: ${response.status}`);
      }

      const data = await response.json();
      setRecommendations(data);
      setView('results');
    } catch (err: any) {
      console.error(err);
      setErrorMessage(err.message || 'Failed to communicate with the Spring Boot backend.');
      setView('error');
    }
  };

  return (
    <>
      {/* Navigation Header */}
      <header className={styles.header}>
        <div className={styles.logo} onClick={() => setView('landing')}>
          <span style={{ fontSize: '24px' }}>🚗</span>
          <span className={styles.logoText}>
            CAR<span>FIND</span>
          </span>
        </div>
        <div className={styles.headerRight}>
          <div className={styles.versionBadge}>
            Decision Engine v2.0
          </div>
          <button
            type="button"
            className="theme-toggle"
            onClick={toggleTheme}
            title={`Switch to ${theme === 'dark' ? 'light' : 'dark'} mode`}
            aria-label="Toggle theme"
          >
            <span className="theme-toggle-icon">{theme === 'dark' ? '☀️' : '🌙'}</span>
            {theme === 'dark' ? 'Light Mode' : 'Dark Mode'}
          </button>
        </div>
      </header>

      {/* Main Workspace */}
      <main className={styles.main}>
        
        {/* LANDING VIEW */}
        {view === 'landing' && (
          <section className={styles.heroSection}>
            <h1 className={styles.heroTitle}>
              Find Your Perfect <br />
              <span>Indian Car Match</span> In 2 Minutes
            </h1>
            
            <p className={styles.heroDesc}>
              Skip browsing infinite specification brochures. Answer 6 rapid questions about your budget, priorities, and features to get mathematically-matched recommendations.
            </p>

            {/* Start CTA Button */}
            <button 
              type="button" 
              className={styles.ctaButton}
              onClick={() => setView('quiz')}
            >
              Launch Decision Engine
            </button>

            {/* App Features Row */}
            <div className={styles.featuresGrid}>
              {[
                { title: '100+ Indian Car Models', desc: 'Covering Maruti, Tata, Hyundai, Mahindra, Toyota, EVs, CNGs, and top features.', icon: '🇮🇳' },
                { title: 'Suitcase Capacity Insights', desc: 'Translates abstract boot volumes (e.g. 382L) into visual luggage items you can pack.', icon: '🧳' },
                { title: 'Dynamic Match Calculations', desc: 'Applies safety weight multipliers, segment adjustments, and low-mileage city penalties.', icon: '📊' }
              ].map(feat => (
                <div key={feat.title} className={styles.featureCard}>
                  <div className={styles.featureIcon}>{feat.icon}</div>
                  <h3 className={styles.featureTitle}>{feat.title}</h3>
                  <p className={styles.featureDesc}>{feat.desc}</p>
                </div>
              ))}
            </div>
          </section>
        )}

        {/* QUIZ VIEW */}
        {view === 'quiz' && (
          <Quiz 
            onSubmit={handleQuizSubmit} 
            onBackToHome={() => setView('landing')} 
          />
        )}

        {/* LOADING VIEW */}
        {view === 'loading' && (
          <div className={styles.loadingContainer}>
            <div className={styles.spinner}></div>
            <h3 className={styles.loadingTitle}>Analyzing car database...</h3>
            <p className={styles.loadingDesc}>
              Calculating match scores across 100+ Indian car variants based on your driving profile.
            </p>
          </div>
        )}

        {/* RESULTS VIEW */}
        {view === 'results' && (
          <Results 
            results={recommendations} 
            onRetry={() => setView('quiz')} 
          />
        )}

        {/* ERROR VIEW */}
        {view === 'error' && (
          <div className={styles.errorCard}>
            <span style={{ fontSize: '40px' }}>⚠️</span>
            <h3 className={styles.errorTitle}>Connection Error</h3>
            <p className={styles.errorText}>
              {errorMessage || `Failed to connect to the backend server. Make sure the Spring Boot backend is running on ${API_BASE}.`}
            </p>
            <div className={styles.errorButtons}>
              <button 
                type="button" 
                className={styles.btnSecondary}
                onClick={() => setView('quiz')}
              >
                Back to Quiz
              </button>
              <button 
                type="button" 
                className={styles.btnPrimary}
                onClick={() => handleQuizSubmit({
                  minBudget: 8,
                  maxBudget: 22,
                  useCase: 'Mixed',
                  familySize: '2-4',
                  preferredFuel: 'No Preference',
                  priorities: [],
                  features: []
                })}
              >
                Retry Request
              </button>
            </div>
          </div>
        )}

      </main>

      {/* Footer */}
      <footer className={styles.footer}>
        © {new Date().getFullYear()} CarFind India. Developed with Spring Boot & React TS.
      </footer>
    </>
  );
}

export default App;
