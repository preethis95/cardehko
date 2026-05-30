import React, { useState } from 'react';
import styles from './Quiz.module.scss';

export interface QuizState {
  minBudget: number;
  maxBudget: number;
  useCase: string;
  familySize: string;
  preferredFuel: string;
  priorities: string[];
  features: string[];
}

interface QuizProps {
  onSubmit: (quizData: QuizState) => void;
  onBackToHome: () => void;
}

const STEP_TOTAL = 6;

export const Quiz: React.FC<QuizProps> = ({ onSubmit, onBackToHome }) => {
  const [step, setStep] = useState(1);
  const [state, setState] = useState<QuizState>({
    minBudget: 8,
    maxBudget: 22,
    useCase: 'Mixed',
    familySize: '2-4',
    preferredFuel: 'No Preference',
    priorities: [],
    features: []
  });

  const handleNext = () => {
    if (step < STEP_TOTAL) {
      setStep(step + 1);
    } else {
      onSubmit(state);
    }
  };

  const handleBack = () => {
    if (step > 1) {
      setStep(step - 1);
    } else {
      onBackToHome();
    }
  };

  const selectSingle = <K extends keyof QuizState>(key: K, value: QuizState[K]) => {
    setState(prev => ({ ...prev, [key]: value }));
  };

  const toggleMulti = <K extends 'priorities' | 'features'>(key: K, item: string) => {
    setState(prev => {
      const currentList = prev[key] as string[];
      const newList = currentList.includes(item)
        ? currentList.filter(x => x !== item)
        : [...currentList, item];
      return { ...prev, [key]: newList };
    });
  };

  const selectPopularBudget = (min: number, max: number) => {
    setState(prev => ({ ...prev, minBudget: min, maxBudget: max }));
  };

  const renderStepContent = () => {
    switch (step) {
      case 1:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>What is your budget range?</h3>
            <p className={styles.stepDesc}>Prices are represented in Lakhs INR (Ex-showroom). Slide to set your minimum and maximum targets.</p>
            
            <div className={styles.sliderWrapper}>
              <div className={styles.sliderLabels}>
                <div>Target Range: <span>₹{state.minBudget}L - ₹{state.maxBudget}L</span></div>
              </div>
              
              <div className={styles.rangeInputs}>
                <div className={styles.rangeField}>
                  <label>Min Budget: ₹{state.minBudget} Lakhs</label>
                  <input 
                    type="range" 
                    min="3" 
                    max="60" 
                    value={state.minBudget} 
                    onChange={e => {
                      const val = parseInt(e.target.value);
                      setState(prev => ({ 
                        ...prev, 
                        minBudget: val,
                        maxBudget: Math.max(val + 1, prev.maxBudget) 
                      }));
                    }}
                  />
                </div>
                
                <div className={styles.rangeField}>
                  <label>Max Budget: ₹{state.maxBudget} Lakhs</label>
                  <input 
                    type="range" 
                    min="5" 
                    max="80" 
                    value={state.maxBudget} 
                    onChange={e => {
                      const val = parseInt(e.target.value);
                      setState(prev => ({ 
                        ...prev, 
                        maxBudget: val,
                        minBudget: Math.min(val - 1, prev.minBudget) 
                      }));
                    }}
                  />
                </div>
              </div>
            </div>

            <div className={styles.popularWrapper}>
              <p>Or select a popular budget tier:</p>
              <div className={styles.popularGrid}>
                {[
                  { label: 'Budget Entry (₹4L - ₹8L)', min: 4, max: 8 },
                  { label: 'Mid-size Commuter (₹8L - ₹15L)', min: 8, max: 15 },
                  { label: 'Premium / Family (₹15L - ₹25L)', min: 15, max: 25 },
                  { label: 'Luxury / EV SUV (₹25L - ₹50L)', min: 25, max: 50 },
                  { label: 'High-End Elite (₹50L+)', min: 50, max: 80 }
                ].map(tier => {
                  const isActive = state.minBudget === tier.min && state.maxBudget === tier.max;
                  return (
                    <button 
                      key={tier.label}
                      type="button" 
                      className={`${styles.popularBtn} ${isActive ? styles.active : ''}`}
                      onClick={() => selectPopularBudget(tier.min, tier.max)}
                    >
                      {tier.label}
                    </button>
                  );
                })}
              </div>
            </div>
          </div>
        );

      case 2:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>What is your primary use-case?</h3>
            <p className={styles.stepDesc}>This helps us optimize efficiency score matching and penalize bulky low-efficiency models for city drivers.</p>
            <div className={styles.grid}>
              {[
                { value: 'City', label: 'City Commute', icon: '🏙️', desc: 'Stop-go traffic, narrow streets' },
                { value: 'Highway', label: 'Highway Cruises', icon: '🛣️', desc: 'High speeds, long distance roadtrips' },
                { value: 'Mixed', label: 'Mixed Travel', icon: '🔄', desc: 'Daily commutes + weekend getaways' }
              ].map(opt => (
                <div 
                  key={opt.value}
                  className={`${styles.optionCard} ${state.useCase === opt.value ? styles.active : ''}`}
                  onClick={() => selectSingle('useCase', opt.value)}
                >
                  <div className={styles.optionIcon}>{opt.icon}</div>
                  <div className={styles.optionLabel}>{opt.label}</div>
                  <span style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>{opt.desc}</span>
                </div>
              ))}
            </div>
          </div>
        );

      case 3:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>How many members in your family?</h3>
            <p className={styles.stepDesc}>We match appropriate body styles (Hatchbacks, Sedans, SUVs, or 7-seater MUVs) based on your family requirements.</p>
            <div className={styles.grid}>
              {[
                { value: '2-4', label: 'Small Family (2-4)', icon: '👨‍👩‍👦', desc: 'Perfect for Hatchbacks & Sedans' },
                { value: '5', label: 'Standard Family (5)', icon: '👨‍👩‍👧‍👦', desc: 'Prefers spacious Sedans & mid-SUVs' },
                { value: '7+', label: 'Large Joint Family (7+)', icon: '👪👪', desc: 'Needs 3-row SUVs and large MUVs' }
              ].map(opt => (
                <div 
                  key={opt.value}
                  className={`${styles.optionCard} ${state.familySize === opt.value ? styles.active : ''}`}
                  onClick={() => selectSingle('familySize', opt.value)}
                >
                  <div className={styles.optionIcon}>{opt.icon}</div>
                  <div className={styles.optionLabel}>{opt.label}</div>
                  <span style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>{opt.desc}</span>
                </div>
              ))}
            </div>
          </div>
        );

      case 4:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>Do you have a fuel preference?</h3>
            <p className={styles.stepDesc}>We have a vast mix of Electric Vehicles (EV), Petrols, Diesels, and factory-fitted CNG kits in the decision engine.</p>
            <div className={styles.grid}>
              {[
                { value: 'Petrol', label: 'Petrol', icon: '⛽', desc: 'Smooth, clean, standard' },
                { value: 'Diesel', label: 'Diesel', icon: '🛢️', desc: 'Torquey, long highway range' },
                { value: 'CNG', label: 'CNG', icon: '🟢', desc: 'Super pocket-friendly commutes' },
                { value: 'EV', label: 'Electric (EV)', icon: '⚡', desc: 'Zero emissions, instant power' },
                { value: 'No Preference', label: 'No Preference', icon: '🔍', desc: 'Suggest all compatible fuels' }
              ].map(opt => (
                <div 
                  key={opt.value}
                  className={`${styles.optionCard} ${state.preferredFuel === opt.value ? styles.active : ''}`}
                  onClick={() => selectSingle('preferredFuel', opt.value)}
                >
                  <div className={styles.optionIcon}>{opt.icon}</div>
                  <div className={styles.optionLabel}>{opt.label}</div>
                </div>
              ))}
            </div>
          </div>
        );

      case 5:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>Select your core driving priorities:</h3>
            <p className={styles.stepDesc}>You can select multiple. Selecting 'Safety' will double safety stars impact in the scoring algorithm!</p>
            <div className={styles.grid}>
              {[
                { value: 'Safety', label: 'Safety First', icon: '🛡️', desc: 'Crash rating, solid builds' },
                { value: 'Mileage', label: 'Maximum Mileage', icon: '📈', desc: 'Save fuel costs, high efficiency' },
                { value: 'Comfort', label: 'Ride & Cabin Comfort', icon: '🛋️', desc: 'Ventilation, spacious setups' },
                { value: 'Performance', label: 'High Performance', icon: '🏎️', desc: 'Turbocharged response, high power' }
              ].map(opt => {
                const isActive = state.priorities.includes(opt.value);
                return (
                  <div 
                    key={opt.value}
                    className={`${styles.optionCard} ${isActive ? styles.active : ''}`}
                    onClick={() => toggleMulti('priorities', opt.value)}
                  >
                    <div className={styles.optionIcon}>{opt.icon}</div>
                    <div className={styles.optionLabel}>{opt.label}</div>
                    <span style={{ fontSize: '11px', color: 'var(--text-muted)', marginTop: '4px' }}>{opt.desc}</span>
                  </div>
                );
              })}
            </div>
          </div>
        );

      case 6:
        return (
          <div className="animate-fade-in">
            <h3 className={styles.stepTitle}>Which features are a must-have?</h3>
            <p className={styles.stepDesc}>Select all tech and luxury features that are dealbreakers for your next new car.</p>
            <div className={styles.grid}>
              {[
                { value: 'sunroof', label: 'Panoramic Sunroof', icon: '🌅' },
                { value: 'adas', label: 'ADAS Safety Suite', icon: '🤖' },
                { value: 'cam360', label: '360° Camera', icon: '👁️' },
                { value: 'ventilatedSeats', label: 'Ventilated Seats', icon: '💨' }
              ].map(opt => {
                const isActive = state.features.includes(opt.value);
                return (
                  <div 
                    key={opt.value}
                    className={`${styles.optionCard} ${isActive ? styles.active : ''}`}
                    onClick={() => toggleMulti('features', opt.value)}
                  >
                    <div className={styles.optionIcon}>{opt.icon}</div>
                    <div className={styles.optionLabel}>{opt.label}</div>
                  </div>
                );
              })}
            </div>
          </div>
        );

      default:
        return null;
    }
  };

  const progressPercentage = (step / STEP_TOTAL) * 100;

  return (
    <div className={styles.quizContainer}>
      <div className={styles.header}>
        <h2>Car Preference Wizard</h2>
        <div className={styles.stepIndicator}>Step {step} of {STEP_TOTAL}</div>
      </div>
      
      <div className={styles.progressBar}>
        <div className={styles.progressFill} style={{ width: `${progressPercentage}%` }}></div>
      </div>

      <div style={{ minHeight: '300px' }}>
        {renderStepContent()}
      </div>

      <div className={styles.footerButtons}>
        <button type="button" className={styles.backBtn} onClick={handleBack}>
          {step === 1 ? 'Exit Quiz' : 'Back'}
        </button>
        <button type="button" className={`${styles.nextBtn} glow-btn`} onClick={handleNext}>
          {step === STEP_TOTAL ? 'Find My Cars' : 'Next'}
        </button>
      </div>
    </div>
  );
};
