/* =========================================================
 * JAMEL : a Java (tm) Agent-based MacroEconomic Main.
 * =========================================================
 *
 * (C) Copyright 2007-2011, Pascal Seppecher.
 * 
 * Project Info <http://p.seppecher.free.fr/jamel/>. 
 *
 * This file is a part of JAMEL (Java Agent-based MacroEconomic Main).
 * 
 * JAMEL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JAMEL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JAMEL. If not, see <http://www.gnu.org/licenses/>.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */

package economicCycle;

import jamel.World;
import jamel.utils.Recordable;
import jamel.utils.TransientNumber;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import scheduling.cycle.Cycle;
import economicCycle.scheduling.events.CreditCheques;
import economicCycle.scheduling.events.PayBankDividend;
import economicCycle.scheduling.events.PayFirmDividends;
import economicCycle.scheduling.events.RunDebtRecovery;
import economicCycle.scheduling.events.RunMarket;
import economicCycle.scheduling.events.RunProduction;
import economicCycle.scheduling.events.UpdateBank;
import economicCycle.scheduling.events.UpdateHouseholds;
import economicCycle.scheduling.events.UpdateProductiveSector;

/**
 * Represents the macro-economic circuit.
 * <p>
 * Last update: 19-Jun-2011
 */
public class EconomicCycle extends Cycle {

	public EconomicCycle(DateTime start, DateTime end, Period step) {
		super(start, end, step);
	}

	public void init(boolean testing) {
		if (!testing) {
			addEvent(new Recordable.PollRecordables(), getStart(), Days.ONE);
			addEvent(new TransientNumber.RefreshTransients(), getStart(),
					Days.ONE);
			addEvent(new PayBankDividend(), getStart().plus(getStep()),
					Days.ONE);
			addEvent(new PayFirmDividends(), getStart().plus(getStep()),
					Days.ONE);// this
			// is
			// done
			// to
			// let
			// firms
			// not
			// pay
			// their
			// first
			// dividend,
			// since
			// dividends
			// are
			// payed
			// before
			// actual
			// money
			// is
			// obtained
			// in
			// this
			// model
			// (and
			// thus,
			// it
			// crashes)
			addEvent(new RunMarket(World.getInstance().getLaborMarket()),
					getStart(), Days.ONE);
			addEvent(new RunProduction(), getStart(), Days.ONE);
			addEvent(new RunMarket(World.getInstance().getGoodsMarket()),
					getStart(), Days.ONE);
			addEvent(new UpdateHouseholds(), getStart(), Days.ONE);
			addEvent(new RunDebtRecovery(), getStart(), Days.ONE);
			addEvent(new UpdateProductiveSector(), getStart(), Days.ONE);
			addEvent(new UpdateBank(), getStart(), Days.ONE);
			addEvent(new CreditCheques(), getStart(), Days.ONE);// TODO: dunno
																// where this
																// goes exactly
		}
	}
}
