import React, { useState, useRef, useEffect, useCallback } from 'react';
import { createPortal } from 'react-dom';
import styles from './Results.module.scss';

export interface Car {
  id: number;
  name: string;
  brand: string;
  priceMin: number;
  priceMax: number;
  safetyRating: number;
  mileage: number;
  fuelType: string;
  segment: string;
  bootSpace: number;
  sunroof: boolean;
  adas: boolean;
  cam360: boolean;
  ventilatedSeats: boolean;
}

export interface RecommendationResult {
  car: Car;
  matchScore: number;
  suitcaseCapacity: string;
}

interface ResultsProps {
  results: RecommendationResult[];
  onRetry: () => void;
}

type SortOption = 'score-desc' | 'price-asc' | 'safety-desc' | 'mileage-desc';
type FuelFilter = 'All' | 'Petrol' | 'Diesel' | 'CNG' | 'EV';
type SegmentFilter = 'All' | 'Hatchback' | 'Sedan' | 'SUV' | 'MUV';

export const Results: React.FC<ResultsProps> = ({ results, onRetry }) => {
  const [sortBy, setSortBy] = useState<SortOption>('score-desc');
  const [fuelFilter, setFuelFilter] = useState<FuelFilter>('All');
  const [segmentFilter, setSegmentFilter] = useState<SegmentFilter>('All');
  const [visibleCount, setVisibleCount] = useState(12);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [menuPos, setMenuPos] = useState({ top: 0, left: 0, width: 0 });
  const dropdownRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLButtonElement>(null);

  const sortOptions: { value: SortOption; label: string; icon: string }[] = [
    { value: 'score-desc', label: 'Best Match Score', icon: '🏆' },
    { value: 'price-asc',  label: 'Price: Low to High', icon: '💰' },
    { value: 'safety-desc', label: 'Safety: High to Low', icon: '🛡️' },
    { value: 'mileage-desc', label: 'Mileage: High to Low', icon: '📈' },
  ];
  const selectedSort = sortOptions.find(o => o.value === sortBy)!;

  /** Compute trigger position so the portal menu can anchor to it */
  const calcMenuPos = useCallback(() => {
    if (triggerRef.current) {
      const r = triggerRef.current.getBoundingClientRect();
      setMenuPos({ top: r.bottom + 6, left: r.left, width: r.width });
    }
  }, []);

  const handleToggle = () => {
    if (!dropdownOpen) calcMenuPos();
    setDropdownOpen(prev => !prev);
  };

  // Close on outside click (must include the portal menu itself)
  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      const target = e.target as Node;
      const menuEl = document.getElementById('sort-dropdown-menu');
      if (
        dropdownRef.current && !dropdownRef.current.contains(target) &&
        !(menuEl && menuEl.contains(target))
      ) {
        setDropdownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Close dropdown on any scroll or resize — standard dropdown UX
  useEffect(() => {
    if (!dropdownOpen) return;
    const close = () => setDropdownOpen(false);
    window.addEventListener('scroll', close, true);
    window.addEventListener('resize', close);
    return () => {
      window.removeEventListener('scroll', close, true);
      window.removeEventListener('resize', close);
    };
  }, [dropdownOpen]);

  // 1. Filter results
  let filteredResults = results.filter(item => {
    const fuelMatch = fuelFilter === 'All' || item.car.fuelType.toLowerCase() === fuelFilter.toLowerCase();
    const segmentMatch = segmentFilter === 'All' || item.car.segment.toLowerCase() === segmentFilter.toLowerCase();
    return fuelMatch && segmentMatch;
  });

  // 2. Sort results
  filteredResults = [...filteredResults].sort((a, b) => {
    switch (sortBy) {
      case 'score-desc':
        return b.matchScore - a.matchScore;
      case 'price-asc':
        return a.car.priceMin - b.car.priceMin;
      case 'safety-desc':
        return b.car.safetyRating - a.car.safetyRating;
      case 'mileage-desc':
        return b.car.mileage - a.car.mileage;
      default:
        return 0;
    }
  });

  // SVG Radial Ring variables
  const radius = 24;
  const circumference = 2 * Math.PI * radius;

  const getScoreColor = (score: number) => {
    if (score >= 85) return '#4CAF50'; // Green
    if (score >= 70) return '#FF9800'; // Amber/Orange
    return '#F44336'; // Red
  };

  const formatPriceRange = (min: number, max: number) => {
    if (min === max) {
      return `₹${min.toFixed(2)} Lakh`;
    }
    return `₹${min.toFixed(2)}L - ₹${max.toFixed(2)}L`;
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>Recommended <span>Matches</span> ({filteredResults.length})</h2>
        <div className={styles.controls}>
          <button type="button" className={styles.btnBack} onClick={onRetry}>
            ← Adjust Preferences
          </button>
          
          {/* Custom Sort Dropdown */}
          <div className={styles.customDropdown} ref={dropdownRef}>
            <button
              ref={triggerRef}
              type="button"
              className={`${styles.dropdownTrigger} ${dropdownOpen ? styles.dropdownTriggerOpen : ''}`}
              onClick={handleToggle}
              aria-haspopup="listbox"
              aria-expanded={dropdownOpen}
            >
              <span className={styles.dropdownSelected}>
                <span className={styles.dropdownIcon}>{selectedSort.icon}</span>
                {selectedSort.label}
              </span>
              <span className={`${styles.dropdownChevron} ${dropdownOpen ? styles.dropdownChevronOpen : ''}`}>
                ▾
              </span>
            </button>
          </div>

          {/* Portal: rendered at document.body — no stacking context issues */}
          {dropdownOpen && createPortal(
            <ul
              id="sort-dropdown-menu"
              className={styles.dropdownMenu}
              role="listbox"
              style={{
                position: 'fixed',
                top: menuPos.top,
                left: menuPos.left,
                width: menuPos.width,
                zIndex: 9999,
              }}
            >
              {sortOptions.map(option => (
                <li
                  key={option.value}
                  role="option"
                  aria-selected={sortBy === option.value}
                  className={`${styles.dropdownItem} ${sortBy === option.value ? styles.dropdownItemActive : ''}`}
                  onClick={() => { setSortBy(option.value); setDropdownOpen(false); }}
                >
                  <span className={styles.dropdownItemIcon}>{option.icon}</span>
                  <span className={styles.dropdownItemLabel}>{option.label}</span>
                  {sortBy === option.value && <span className={styles.dropdownItemCheck}>✓</span>}
                </li>
              ))}
            </ul>,
            document.body
          )}
        </div>
      </div>

      {/* Quick Filter Badges */}
      <div className={styles.filterBar}>
        <span style={{ fontSize: '12px', fontWeight: 600, color: 'var(--text-muted)', display: 'flex', alignItems: 'center', marginRight: '8px' }}>Fuel:</span>
        {(['All', 'Petrol', 'Diesel', 'CNG', 'EV'] as FuelFilter[]).map(fuel => (
          <div 
            key={fuel}
            className={`${styles.filterBadge} ${fuelFilter === fuel ? styles.active : ''}`}
            onClick={() => { setFuelFilter(fuel); setVisibleCount(12); }}
          >
            {fuel}
          </div>
        ))}
        
        <span style={{ fontSize: '12px', fontWeight: 600, color: 'var(--text-muted)', display: 'flex', alignItems: 'center', marginLeft: '16px', marginRight: '8px' }}>Style:</span>
        {(['All', 'Hatchback', 'Sedan', 'SUV', 'MUV'] as SegmentFilter[]).map(seg => (
          <div 
            key={seg}
            className={`${styles.filterBadge} ${segmentFilter === seg ? styles.active : ''}`}
            onClick={() => { setSegmentFilter(seg); setVisibleCount(12); }}
          >
            {seg}
          </div>
        ))}
      </div>

      {filteredResults.length === 0 ? (
        <div className={styles.noResults}>
          <h3>No perfect matches found.</h3>
          <p>Try widening your budget range or choosing "No Preference" on fuel types.</p>
          <button type="button" className={styles.btnBack} onClick={onRetry}>
            Adjust Preferences
          </button>
        </div>
      ) : (
        <>
          <div className={styles.grid}>
            {filteredResults.slice(0, visibleCount).map((item, idx) => {
              const strokeDashoffset = circumference - (item.matchScore / 100) * circumference;
              const ringColor = getScoreColor(item.matchScore);
              const isFirstMatch = idx === 0 && sortBy === 'score-desc' && item.matchScore >= 80;

              return (
                <div key={item.car.id} className={`${styles.card} ${isFirstMatch ? styles.topMatch : ''}`}>
                  {isFirstMatch && <div className={styles.topMatchRibbon}>Best Match</div>}
                  
                  <div className={styles.cardHeader}>
                    <div className={styles.brandInfo}>
                      <div className={styles.brandName}>{item.car.brand}</div>
                      <div className={styles.carName}>{item.car.name}</div>
                    </div>

                    {/* Radial score ring */}
                    <div className={styles.scoreWrapper}>
                      <svg className={styles.scoreSvg}>
                        <circle className={styles.ringBg} cx="30" cy="30" r={radius} />
                        <circle 
                          className={styles.ringFill} 
                          cx="30" 
                          cy="30" 
                          r={radius} 
                          stroke={ringColor}
                          strokeDasharray={circumference}
                          strokeDashoffset={strokeDashoffset}
                        />
                      </svg>
                      <div className={styles.scoreText}>
                        {item.matchScore}<span>%</span>
                      </div>
                    </div>
                  </div>

                  {/* Pricing and basic specs */}
                  <div className={styles.specsList}>
                    <div className={styles.specItem}>
                      <span className={styles.label}>Ex-Showroom Price</span>
                      <span className={styles.value} style={{ color: 'var(--primary-red)', fontFamily: 'var(--display-font)', fontWeight: '700' }}>
                        {formatPriceRange(item.car.priceMin, item.car.priceMax)}
                      </span>
                    </div>
                    <div className={styles.specItem}>
                      <span className={styles.label}>Safety Rating</span>
                      <span className={styles.value}>
                        {item.car.safetyRating > 0 ? (
                          <span className={styles.stars}>{'★'.repeat(item.car.safetyRating)}</span>
                        ) : (
                          <span style={{ fontSize: '11px', color: 'var(--text-muted)', fontWeight: 500 }}>Unrated</span>
                        )}
                      </span>
                    </div>
                    <div className={styles.specItem}>
                      <span className={styles.label}>Fuel Type</span>
                      <span className={styles.value}>{item.car.fuelType}</span>
                    </div>
                    <div className={styles.specItem}>
                      <span className={styles.label}>Mileage / Range</span>
                      <span className={styles.value}>
                        {item.car.mileage} {item.car.fuelType === 'EV' ? 'km/chg' : 'km/l'}
                      </span>
                    </div>
                  </div>

                  {/* Luggage Capacity Insight */}
                  <div className={styles.luggageInsight}>
                    <span className={styles.luggageIcon}>🧳</span>
                    <div className={styles.luggageText}>
                      <strong>{item.suitcaseCapacity}</strong>
                      Boot capacity of {item.car.bootSpace} Litres
                    </div>
                  </div>

                  {/* Dynamic checklist of key features */}
                  <div className={styles.featuresGrid}>
                    <span className={`${styles.featureBadge} ${item.car.sunroof ? styles.hasFeature : ''}`}>
                      Sunroof
                    </span>
                    <span className={`${styles.featureBadge} ${item.car.adas ? styles.hasFeature : ''}`}>
                      ADAS
                    </span>
                    <span className={`${styles.featureBadge} ${item.car.cam360 ? styles.hasFeature : ''}`}>
                      360° Cam
                    </span>
                    <span className={`${styles.featureBadge} ${item.car.ventilatedSeats ? styles.hasFeature : ''}`}>
                      Ventilated
                    </span>
                  </div>
                </div>
              );
            })}
          </div>

          {filteredResults.length > visibleCount && (
            <div style={{ textAlign: 'center', marginBottom: '80px' }}>
              <button 
                type="button" 
                className={styles.btnBack} 
                onClick={() => setVisibleCount(prev => prev + 12)}
                style={{ padding: '12px 30px', fontSize: '15px' }}
              >
                Load More Models
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};
