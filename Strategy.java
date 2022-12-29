import java.util.List;

interface Strategy {
	public Grade getBestScore(List<Grade> grades);
}

class BestPartialScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getPartialScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getPartialScore()) {
				maxPartialScore = grades.get(i).getPartialScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}

class BestExamScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getExamScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getExamScore()) {
				maxPartialScore = grades.get(i).getExamScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}

class BestTotalScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getTotal();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getTotal()) {
				maxPartialScore = grades.get(i).getTotal();
				index = i;
			}
		}
		
		return grades.get(index);
	}	
}