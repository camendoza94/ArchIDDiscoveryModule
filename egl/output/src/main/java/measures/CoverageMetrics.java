package measures;

/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Collections;
import java.util.List;

public class CoverageMetrics implements Metrics {

    static final Metric<Integer> ARCHITECTURAL_TECHNICAL_DEBT = new Metric.Builder("arch-debt", "Architectural debt", Metric.ValueType.WORK_DUR)
            .setDescription("Technical debt for architectural bad smells.")
            .setDirection(Metric.DIRECTION_WORST)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return Collections.singletonList(ARCHITECTURAL_TECHNICAL_DEBT);
    }
}

